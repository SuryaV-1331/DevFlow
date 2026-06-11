package com.devflow.devflow.service;

import com.devflow.devflow.dto.ActionRequest;
import com.devflow.devflow.dto.ApprovalHistoryResponse;
import com.devflow.devflow.dto.RequestCreateRequest;
import com.devflow.devflow.dto.RequestResponse;
import com.devflow.devflow.entity.*;
import com.devflow.devflow.enums.RequestStatus;
import com.devflow.devflow.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;


@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final WorkflowMasterRepository workflowMasterRepository;
    private final WorkflowStepRepository workflowStepRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;

    public RequestService(
            RequestRepository requestRepository,
            UserRepository userRepository,
            WorkflowMasterRepository workflowMasterRepository, WorkflowStepRepository workflowStepRepository, ApprovalHistoryRepository approvalHistoryRepository) {

        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.workflowMasterRepository = workflowMasterRepository;
        this.workflowStepRepository = workflowStepRepository;
        this.approvalHistoryRepository = approvalHistoryRepository;
    }

    public Request createRequest(
            RequestCreateRequest request) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        WorkflowMaster workflow =
                workflowMasterRepository
                        .findById(request.getWorkflowId())
                        .orElseThrow(() ->
                                new RuntimeException("Workflow not found"));

        Request requestEntity = new Request();

        requestEntity.setStatus(RequestStatus.PENDING);
        requestEntity.setCurrentStep(1);
        requestEntity.setCreatedBy(user);
        requestEntity.setWorkflow(workflow);
        requestEntity.setCreatedAt(
                LocalDateTime.now());
        requestEntity.setUpdatedAt(
                LocalDateTime.now());

        return requestRepository.save(requestEntity);
    }

    public Request approveRequest(Long requestId,
                                  ActionRequest actionRequest) {

        Request request =
                requestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        validateApprovalPermission(request);

        Integer nextStep =
                request.getCurrentStep() + 1;

        Optional<WorkflowStep> nextWorkflowStep =
                workflowStepRepository
                        .findByWorkflowIdAndStepOrder(
                                request.getWorkflow().getId(),
                                nextStep
                        );

        if (nextWorkflowStep.isPresent()) {

            request.setCurrentStep(nextStep);

        } else {

            request.setStatus(RequestStatus.APPROVED);
        }

        saveHistory(
                request,
                "APPROVED",
                actionRequest.getComment()
        );

        request.setUpdatedAt(
                LocalDateTime.now());

        return requestRepository.save(request);
    }

    public List<RequestResponse> getMyRequests(){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        return requestRepository
                .findByCreatedById(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RequestResponse> getAllRequests() {

        return requestRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RequestResponse mapToResponse(
            Request request) {

        RequestResponse response =
                new RequestResponse();

        response.setId(request.getId());
        response.setStatus(request.getStatus());
        response.setCurrentStep(
                request.getCurrentStep());

        response.setWorkflowName(
                request.getWorkflow()
                        .getWorkflowName());

        response.setCreatedBy(
                request.getCreatedBy()
                        .getName());

        response.setCreatedAt(
                request.getCreatedAt());

        response.setUpdatedAt(
                request.getUpdatedAt());

        return response;
    }

    public List<RequestResponse> getPendingRequests() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        Role curRole = user.getRole();

        List<RequestResponse> responses =
                new ArrayList<>();

        for (Request req : requestRepository.findAll()) {

            Integer curStep = req.getCurrentStep();

            WorkflowStep currentWorkflowStep =
                    workflowStepRepository
                            .findByWorkflowIdAndStepOrder(
                                    req.getWorkflow().getId(),
                                    curStep
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Workflow step not found"));

            if (req.getStatus() == RequestStatus.PENDING
                    && currentWorkflowStep.getRole().equals(curRole)) {

                responses.add(
                        mapToResponse(req)
                );
            }
        }

        return responses;
    }

    private void validateApprovalPermission(
            Request request){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        WorkflowStep currentWorkflowStep =
                workflowStepRepository
                        .findByWorkflowIdAndStepOrder(
                                request.getWorkflow().getId(),
                                request.getCurrentStep()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Workflow step not found"));

        if (!user.getRole().getName().equals(
                currentWorkflowStep.getRole().getName())) {

            throw new RuntimeException(
                    "You are not authorized to approve this step");
        }
    }

    public Request rejectRequest(Long requestId,
                                 ActionRequest actionRequest) {

        Request request =
                requestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        validateApprovalPermission(request);

        request.setStatus(RequestStatus.REJECTED);

        saveHistory(
                request,
                "REJECTED",
                actionRequest.getComment()
        );

        request.setUpdatedAt(
                LocalDateTime.now());

        return requestRepository.save(request);
    }

    private void saveHistory(
            Request request,
            String action,
            String comment) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        ApprovalHistory history =
                new ApprovalHistory();

        history.setRequest(request);
        history.setStepOrder(
                request.getCurrentStep()
        );
        history.setActionBy(user);
        history.setComment(comment);
        history.setAction(action);
        history.setActionTime(LocalDateTime.now());

        approvalHistoryRepository.save(history);
    }

    private ApprovalHistoryResponse mapHistory(
            ApprovalHistory history) {

        ApprovalHistoryResponse response =
                new ApprovalHistoryResponse();

        response.setAction(
                history.getAction());

        response.setStepOrder(
                history.getStepOrder());

        response.setActionBy(
                history.getActionBy().getName());

        response.setActionTime(
                history.getActionTime());

        response.setComment(
                history.getComment());

        return response;
    }

    public List<ApprovalHistoryResponse>
    getRequestHistory(Long requestId) {

        return approvalHistoryRepository
                .findByRequestIdOrderByActionTime(
                        requestId)
                .stream()
                .map(this::mapHistory)
                .toList();
    }

    public RequestResponse getRequestById(
            Long requestId) {

        Request request =
                requestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"));

        return mapToResponse(request);
    }
}