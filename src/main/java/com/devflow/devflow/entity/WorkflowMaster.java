package com.devflow.devflow.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "workflow_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String workflowName;

    private String workflowDescription;
}
