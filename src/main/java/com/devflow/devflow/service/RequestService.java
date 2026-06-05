package com.devflow.devflow.service;

import com.devflow.devflow.dto.RequestCreateRequest;
import com.devflow.devflow.entity.Request;
import com.devflow.devflow.entity.User;
import com.devflow.devflow.entity.WorkflowMaster;
import com.devflow.devflow.repository.RequestRepository;
import com.devflow.devflow.repository.UserRepository;
import com.devflow.devflow.repository.WorkflowMasterRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final WorkflowMasterRepository workflowMasterRepository;

    public RequestService(
            RequestRepository requestRepository,
            UserRepository userRepository,
            WorkflowMasterRepository workflowMasterRepository) {

        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.workflowMasterRepository = workflowMasterRepository;
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
        requestEntity.setCreatedBy(user);
        requestEntity.setWorkflow(workflow);

        return requestRepository.save(requestEntity);
    }
}