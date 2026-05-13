package com.services.driver.service;

import com.services.driver.dto.request.AddLicenseRequest;
import com.services.driver.dto.request.UpdateLicenseExpirationRequest;
import com.services.driver.dto.response.LicenseResponse;
import com.services.driver.exception.DuplicateLicenseCategoryException;
import com.services.driver.exception.DriverNotFoundException;
import com.services.driver.exception.InvalidLicenseExpirationException;
import com.services.driver.mapper.LicenseMapper;
import com.services.driver.model.Driver;
import com.services.driver.model.License;
import com.services.driver.model.LicenseCategory;
import com.services.driver.repository.DriverRepository;
import com.services.driver.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final DriverRepository driverRepository;
    private final LicenseMapper licenseMapper;
    private final AuditService auditService;

    // ── Consultas ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LicenseResponse> getLicensesByDriver(UUID driverId) {
        validateDriverExists(driverId);
        return licenseMapper.toResponseList(licenseRepository.findByDriverId(driverId));
    }

    // ── Agregar categoría a conductor existente ───────────────────────────────

    @Transactional
    public LicenseResponse addLicense(UUID driverId, AddLicenseRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Conductor no encontrado con id: " + driverId));

        validateNoDuplicateCategory(driverId, request.getCategory());
        validateExpirationDate(driver.getBirthDate(), request.getCategory(),
                request.getIssueDate(), request.getExpirationDate());

        License license = new License();
        license.setDriver(driver);
        license.setCategory(request.getCategory());
        license.setIssueDate(request.getIssueDate());
        license.setExpirationDate(request.getExpirationDate());

        License saved = licenseRepository.save(license);
        auditService.logLicenseInsert(saved.getIdLicense(), currentUser());
        return licenseMapper.toResponse(saved);
    }

    // ── Renovar fecha de vencimiento ────────────────────────────────

    @Transactional
    public LicenseResponse updateExpiration(UUID driverId, UUID licenseId,
                                            UpdateLicenseExpirationRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Conductor no encontrado con id: " + driverId));

        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Licencia no encontrada con id: " + licenseId));

        // Verificar que la licencia pertenece al conductor indicado
        if (!license.getDriver().getIdDriver().equals(driverId)) {
            throw new DriverNotFoundException(
                    "La licencia no pertenece al conductor indicado");
        }

        validateExpirationDate(driver.getBirthDate(), license.getCategory(),
                license.getIssueDate(), request.getExpirationDate());

        String oldValue = license.getExpirationDate().toString();
        license.setExpirationDate(request.getExpirationDate());
        License updated = licenseRepository.save(license);

        auditService.logLicenseUpdate(licenseId, "expiration_date",
                oldValue, request.getExpirationDate().toString(), currentUser());

        return licenseMapper.toResponse(updated);
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    // Valida que el conductor no tenga ya registrada esa categoría.
    public void validateNoDuplicateCategory(UUID driverId, LicenseCategory category) {
        if (licenseRepository.existsByDriver_IdDriverAndCategory(driverId, category)) {
            throw new DuplicateLicenseCategoryException(
                    "El conductor ya tiene registrada la categoría: " + category);
        }
    }

    // Valida que la fecha de vencimiento no exceda la vigencia máxima
    // Particular (A1,A2,B1,B2,B3): <60 años → 10 años | 60-79 → 5 años | ≥80 → 1 año
    // Público    (C1,C2,C3):       <60 años →  3 años | ≥60   → 1 año
    public void validateExpirationDate(LocalDate birthDate, LicenseCategory category,
                                       LocalDate issueDate, LocalDate expirationDate) {
        if (!expirationDate.isAfter(issueDate)) {
            throw new InvalidLicenseExpirationException(
                    "La fecha de vencimiento debe ser posterior a la fecha de expedición");
        }

        int ageAtIssue = Period.between(birthDate, issueDate).getYears();
        int maxYears = category.maxValidityYears(ageAtIssue);
        LocalDate maxAllowed = issueDate.plusYears(maxYears);

        if (expirationDate.isAfter(maxAllowed)) {
            throw new InvalidLicenseExpirationException(String.format(
                    "La vigencia máxima para la categoría %s con %d años es %d año(s). " +
                            "Fecha máxima permitida: %s",
                    category, ageAtIssue, maxYears, maxAllowed));
        }
    }

    // Valida que un conjunto de licencias a crear juntas no tenga categorías repetidas.
    // Usado al registrar un conductor con múltiples licencias en la misma operación.
    public void validateNoDuplicateCategoriesInRequest(
            List<? extends Object> licenses,
            java.util.function.Function<Object, LicenseCategory> categoryExtractor) {

        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) licenses;
        Set<LicenseCategory> seen = new java.util.HashSet<>();
        for (Object item : list) {
            LicenseCategory cat = categoryExtractor.apply(item);
            if (!seen.add(cat)) {
                throw new DuplicateLicenseCategoryException(
                        "El request contiene la categoría duplicada: " + cat);
            }
        }
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    private void validateDriverExists(UUID driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException("Conductor no encontrado con id: " + driverId);
        }
    }

    private String currentUser() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}