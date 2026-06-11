package com.devflow.devflow.service;

import com.devflow.devflow.dto.WorkflowStepRequest;
import com.devflow.devflow.entity.Role;
import com.devflow.devflow.entity.WorkflowMaster;
import com.devflow.devflow.entity.WorkflowStep;
import com.devflow.devflow.repository.RoleRepository;
import com.devflow.devflow.repository.WorkflowMasterRepository;
import com.devflow.devflow.repository.WorkflowStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowStepService {

    private final WorkflowMasterRepository workflowMasterRepository;
    private final WorkflowStepRepository workflowStepRepository;
    private final RoleRepository roleRepository;

    public WorkflowStepService(WorkflowMasterRepository workflowMasterRepository, WorkflowStepRepository workflowStepRepository, RoleRepository roleRepository) {
        this.workflowMasterRepository = workflowMasterRepository;
        this.workflowStepRepository = workflowStepRepository;
        this.roleRepository = roleRepository;
    }

    public WorkflowStep createStep(
            WorkflowStepRequest request){

        WorkflowMaster workflow =
                workflowMasterRepository
                        .findById(request.getWorkflowId())
                        .orElseThrow(() ->
                                new RuntimeException("Workflow not found"));

        Role role =
                roleRepository
                        .findById(request.getRoleId())
                        .orElseThrow(() ->
                                new RuntimeException("Role not found"));

        WorkflowStep step = new WorkflowStep();

        step.setStepOrder(request.getStepOrder());
        step.setWorkflow(workflow);
        step.setRole(role);

        return workflowStepRepository.save(step);
    }

    public List<WorkflowStep> getSteps(
            Long workflowId) {

        return workflowStepRepository
                .findByWorkflowIdOrderByStepOrder(
                        workflowId);
    }

    public List<WorkflowStep>
    getWorkflowSteps(
            Long workflowId) {

        return workflowStepRepository
                .findByWorkflowIdOrderByStepOrder(
                        workflowId);
    }

}
