package com.devflow.devflow.service;

import com.devflow.devflow.dto.RequestCreateRequest;
import com.devflow.devflow.dto.RequestResponse;
import com.devflow.devflow.entity.*;
import com.devflow.devflow.repository.RequestRepository;
import com.devflow.devflow.repository.UserRepository;
import com.devflow.devflow.repository.WorkflowMasterRepository;
import com.devflow.devflow.repository.WorkflowStepRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;


@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final WorkflowMasterRepository workflowMasterRepository;
    private final WorkflowStepRepository workflowStepRepository;

    public RequestService(
            RequestRepository requestRepository,
            UserRepository userRepository,
            WorkflowMasterRepository workflowMasterRepository, WorkflowStepRepository workflowStepRepository) {

        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.workflowMasterRepository = workflowMasterRepository;
        this.workflowStepRepository = workflowStepRepository;
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

        requestEntity.setStatus("PENDING");
        requestEntity.setCurrentStep(1);
        requestEntity.setCreatedBy(user);
        requestEntity.setWorkflow(workflow);

        return requestRepository.save(requestEntity);
    }

    public Request approveRequest(Long requestId) {

        Request request =
                requestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

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

            request.setStatus("APPROVED");
        }

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

            if ("PENDING".equals(req.getStatus())
                    && currentWorkflowStep.getRole().equals(curRole)) {

                responses.add(
                        mapToResponse(req)
                );
            }
        }

        return responses;
    }
}