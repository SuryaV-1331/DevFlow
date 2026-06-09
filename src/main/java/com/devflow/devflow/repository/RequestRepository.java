package com.devflow.devflow.repository;

import com.devflow.devflow.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository
    extends JpaRepository<Request, Long> {

    List<Request> findByCreatedById(Long userId);
}
