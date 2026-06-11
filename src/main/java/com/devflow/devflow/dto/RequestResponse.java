package com.devflow.devflow.dto;

import com.devflow.devflow.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestResponse {

    private Long id;

    private RequestStatus status;

    private Integer currentStep;

    private String workflowName;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}