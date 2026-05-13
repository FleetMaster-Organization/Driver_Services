package com.services.driver.service;

import com.services.driver.model.audit.DriverAudit;
import com.services.driver.model.audit.EmergencyContactAudit;
import com.services.driver.model.audit.LicenseAudit;
import com.services.driver.repository.audit.DriverAuditRepository;
import com.services.driver.repository.audit.EmergencyContactAuditRepository;
import com.services.driver.repository.audit.LicenseAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final DriverAuditRepository driverAuditRepository;
    private final LicenseAuditRepository licenseAuditRepository;
    private final EmergencyContactAuditRepository emergencyContactAuditRepository;

    // ── Driver ───────────────────────────────────────────────────────────────

    @Transactional(propagation = Propagation.MANDATORY)
    public void logDriverInsert(UUID driverId, String modifiedBy) {
        DriverAudit audit = new DriverAudit();
        audit.setIdDriver(driverId);
        audit.setActionType("INSERT");
        audit.setModifiedBy(modifiedBy);
        driverAuditRepository.save(audit);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logDriverUpdate(UUID driverId, String field, String oldValue, String newValue, String modifiedBy) {
        DriverAudit audit = new DriverAudit();
        audit.setIdDriver(driverId);
        audit.setActionType("UPDATE");
        audit.setModifiedField(field);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setModifiedBy(modifiedBy);
        driverAuditRepository.save(audit);
    }

    // ── License ──────────────────────────────────────────────────────────────

    @Transactional(propagation = Propagation.MANDATORY)
    public void logLicenseInsert(UUID licenseId, String modifiedBy) {
        LicenseAudit audit = new LicenseAudit();
        audit.setIdLicense(licenseId);
        audit.setActionType("INSERT");
        audit.setModifiedBy(modifiedBy);
        licenseAuditRepository.save(audit);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logLicenseUpdate(UUID licenseId, String field, String oldValue, String newValue, String modifiedBy) {
        LicenseAudit audit = new LicenseAudit();
        audit.setIdLicense(licenseId);
        audit.setActionType("UPDATE");
        audit.setModifiedField(field);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setModifiedBy(modifiedBy);
        licenseAuditRepository.save(audit);
    }

    // ── EmergencyContact ─────────────────────────────────────────────────────

    @Transactional(propagation = Propagation.MANDATORY)
    public void logContactInsert(UUID contactId, String modifiedBy) {
        EmergencyContactAudit audit = new EmergencyContactAudit();
        audit.setIdContact(contactId);
        audit.setActionType("INSERT");
        audit.setModifiedBy(modifiedBy);
        emergencyContactAuditRepository.save(audit);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void logContactUpdate(UUID contactId, String field, String oldValue, String newValue, String modifiedBy) {
        EmergencyContactAudit audit = new EmergencyContactAudit();
        audit.setIdContact(contactId);
        audit.setActionType("UPDATE");
        audit.setModifiedField(field);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setModifiedBy(modifiedBy);
        emergencyContactAuditRepository.save(audit);
    }
}