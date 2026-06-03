package com.devflow.devflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
