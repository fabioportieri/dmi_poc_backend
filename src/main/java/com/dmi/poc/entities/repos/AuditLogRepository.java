package com.dmi.poc.entities.repos;

import com.dmi.poc.entities.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogs, Long>, JpaSpecificationExecutor<AuditLogs> {
}
