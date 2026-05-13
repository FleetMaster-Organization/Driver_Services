package com.services.driver.repository.audit;

import com.services.driver.model.audit.DriverAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAuditRepository extends JpaRepository<DriverAudit, Long> {
}