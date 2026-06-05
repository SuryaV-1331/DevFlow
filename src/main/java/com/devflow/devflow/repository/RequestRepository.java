package com.devflow.devflow.repository;

import com.devflow.devflow.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository
    extends JpaRepository<Request, Long> {
}
