package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class WorkflowStepRequest {

    private Long workflowId;

    private Long roleId;

    private Integer stepOrder;
}
