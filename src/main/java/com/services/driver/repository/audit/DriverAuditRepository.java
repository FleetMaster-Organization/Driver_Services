package com.services.driver.repository.audit;

import com.services.driver.model.audit.DriverAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverAuditRepository extends JpaRepository<DriverAudit, Long> {

    List<DriverAudit> findByIdDriverOrderByModifiedAtDesc(UUID idDriver);
}