package com.services.driver.repository.audit;

import com.services.driver.model.audit.LicenseAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseAuditRepository extends JpaRepository<LicenseAudit, Long> {

}