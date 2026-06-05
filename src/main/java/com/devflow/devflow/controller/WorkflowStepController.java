package com.devflow.devflow.controller;

import com.devflow.devflow.dto.WorkflowStepRequest;
import com.devflow.devflow.entity.WorkflowStep;
import com.devflow.devflow.service.WorkflowStepService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflow-steps")
public class WorkflowStepController {

    private final WorkflowStepService workflowStepService;

    public WorkflowStepController(WorkflowStepService workflowStepService) {
        this.workflowStepService = workflowStepService;
    }

    @PostMapping
    public WorkflowStep createStep(
            @RequestBody WorkflowStepRequest request){

        return workflowStepService.createStep(request);
    }

    @GetMapping("/{workflowId}")
    public List<WorkflowStep> getSteps(
            @PathVariable Long workflowId) {

        return workflowStepService
                .getSteps(workflowId);
    }
}
