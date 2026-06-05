package com.devflow.devflow.service;

import com.devflow.devflow.dto.WorkflowMasterRequest;
import com.devflow.devflow.entity.WorkflowMaster;
import com.devflow.devflow.repository.WorkflowMasterRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkflowMasterService {

    private final WorkflowMasterRepository workflowMasterRepository;

    public WorkflowMasterService(WorkflowMasterRepository workflowMasterRepository) {
        this.workflowMasterRepository = workflowMasterRepository;
    }

    public WorkflowMaster createWorkflow(
            WorkflowMasterRequest request){

        WorkflowMaster workflow =
                new WorkflowMaster();

        workflow.setWorkflowName(request.getWorkflowName());

        workflow.setWorkflowDescription(request.getWorkflowDescription());

        return workflowMasterRepository.save(workflow);
    }
}
