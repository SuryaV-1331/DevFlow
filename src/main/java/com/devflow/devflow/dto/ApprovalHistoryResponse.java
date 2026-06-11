package com.devflow.devflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalHistoryResponse {

    private String action;

    private Integer stepOrder;

    private String actionBy;

    private String comment;

    private LocalDateTime actionTime;
}
