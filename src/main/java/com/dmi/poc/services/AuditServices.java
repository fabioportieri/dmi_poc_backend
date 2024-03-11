package com.dmi.poc.services;

import com.dmi.poc.dto.PagedResponse;
import com.dmi.poc.entities.AuditLogs;
import com.dmi.poc.entities.repos.AuditLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServices {

    final
    AuditLogRepository auditLogRepository;

    public AuditServices(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logMessage(String operation, String message) {
        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setMessage(message);
        auditLogs.setOperation(operation);

        this.auditLogRepository.save(auditLogs);
    }

    public PagedResponse<AuditLogs> searchLogs(String operation, String query) {
        if(StringUtils.isNotEmpty(operation)) {

        }
        List<AuditLogs> results = this.auditLogRepository.findAll(prepareSpecs(operation, query));
        long count = this.auditLogRepository.count(prepareSpecs(operation, query));

        PagedResponse<AuditLogs> response = new PagedResponse();
        response.setContent(results);
        response.setSize((int) count);
        return response;
    }

    private Specification<AuditLogs> prepareSpecs(String operation, String query) {
        return ((root, query1, criteriaBuilder) -> {
            List<Predicate> prList = new ArrayList<>();
            if(StringUtils.isNotEmpty(operation)) {
                prList.add(criteriaBuilder.equal(root.get("operation"), operation));
            }
            if(StringUtils.isNotEmpty(query)) {
                prList.add(criteriaBuilder.like(root.get("message"), "%"+query+"%"));
            }
            return criteriaBuilder.and(prList.toArray(new Predicate[]{}));
        });
    }
}
