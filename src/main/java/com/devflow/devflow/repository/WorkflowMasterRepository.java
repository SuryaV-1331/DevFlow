package com.devflow.devflow.repository;

import com.devflow.devflow.entity.WorkflowMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowMasterRepository
        extends JpaRepository<WorkflowMaster, Long> {

}
