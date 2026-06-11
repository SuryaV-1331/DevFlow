package com.devflow.devflow.controller;

import com.devflow.devflow.dto.WorkflowMasterRequest;
import com.devflow.devflow.entity.WorkflowMaster;
import com.devflow.devflow.service.WorkflowMasterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflows")
public class WorkflowMasterController {

    private final WorkflowMasterService workflowMasterService;

    public WorkflowMasterController(WorkflowMasterService workflowMasterService) {
        this.workflowMasterService = workflowMasterService;
    }

    @PostMapping
    public WorkflowMaster createWorkflow(
            @RequestBody WorkflowMasterRequest request) {

        return workflowMasterService.createWorkflow(request);
    }

    @GetMapping
    public List<WorkflowMaster> getAllWorkflows() {

        return workflowMasterService
                .getAllWorkflows();
    }

    @GetMapping("/{workflowId}")
    public WorkflowMaster getWorkflowById(
            @PathVariable Long workflowId) {

        return workflowMasterService
                .getWorkflowById(workflowId);
    }
}
