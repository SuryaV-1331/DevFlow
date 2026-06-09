package com.devflow.devflow.repository;

import com.devflow.devflow.entity.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowStepRepository
        extends JpaRepository<WorkflowStep, Long> {

    List<WorkflowStep> findByWorkflowIdOrderByStepOrder(
            Long workflowId);

    Optional<WorkflowStep> findByWorkflowIdAndStepOrder(
            Long workflowId,
            Integer stepOrder
    );
}
