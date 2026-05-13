package com.services.driver.repository.audit;

import com.services.driver.model.audit.EmergencyContactAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyContactAuditRepository extends JpaRepository<EmergencyContactAudit, Long> {

}