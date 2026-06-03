package com.devflow.devflow.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private WorkflowMaster workflow;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
