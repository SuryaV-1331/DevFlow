package com.devflow.devflow.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "workflow_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stepOrder;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private WorkflowMaster workflow;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
