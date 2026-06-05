package com.devflow.devflow.controller;

import com.devflow.devflow.dto.WorkflowMasterRequest;
import com.devflow.devflow.entity.WorkflowMaster;
import com.devflow.devflow.service.WorkflowMasterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
