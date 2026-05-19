package com.planner.backend.repository;

import com.planner.backend.model.ErrorReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorReportRepository extends JpaRepository<ErrorReport, Long> {
}
