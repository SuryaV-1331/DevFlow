package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class RequestResponse {

    private Long id;

    private String status;

    private Integer currentStep;

    private String workflowName;

    private String createdBy;
}