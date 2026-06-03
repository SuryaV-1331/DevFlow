package com.devflow.devflow.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "request_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "workflow_step_id")
    private WorkflowStep workflowStep;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;
}
