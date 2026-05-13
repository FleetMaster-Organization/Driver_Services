package com.services.driver.service;

import com.services.driver.dto.request.CreateDriverRequest;
import com.services.driver.dto.request.UpdateDriverPersonalRequest;
import com.services.driver.dto.request.UpdateDriverStatusRequest;
import com.services.driver.dto.response.DriverResponse;
import com.services.driver.dto.response.DriverSummaryResponse;
import com.services.driver.dto.response.SubstatusResponse;
import com.services.driver.exception.DriverInRouteException;
import com.services.driver.exception.DriverNotFoundException;
import com.services.driver.exception.DuplicateIdCardException;
import com.services.driver.exception.SubstatusNotFoundException;
import com.services.driver.mapper.DriverMapper;
import com.services.driver.model.*;
import com.services.driver.repository.DriverRepository;
import com.services.driver.repository.EmploymentSubstatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final EmploymentSubstatusRepository substatusRepository;
    private final DriverMapper driverMapper;
    private final LicenseService licenseService;
    private final AuditService auditService;

    // ── Consultas ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<DriverSummaryResponse> getAllDrivers() {
        return driverRepository.findAllWithSubstatus()
                .stream()
                .map(driverMapper::toSummaryResponse)
                .toList();
    }

    // Detalle completo de un conductor.
    @Transactional(readOnly = true)
    public DriverResponse getDriverById(UUID id) {
        Driver driver = findWithSubstatusOrThrow(id);
        return driverMapper.toResponse(driver);
    }

    // Lista todos los subestados disponibles para el selector del frontend.
    @Transactional(readOnly = true)
    public List<SubstatusResponse> getAllSubstatuses() {
        return substatusRepository.findAllWithStatus().stream()
                .map(sub -> SubstatusResponse.builder()
                        .idSubstatus(sub.getIdSubstatus())
                        .statusName(sub.getEmploymentStatus().getStatusName())
                        .substatusName(sub.getSubstatusName())
                        .build())
                .toList();
    }

    // ── Creación ─────────────────────────────────────────────


    // 1. Registra un nuevo conductor con sus licencias y contacto de emergencia.
    @Transactional
    public DriverResponse createDriver(CreateDriverRequest request) {
        // 1. Cédula única
        if (driverRepository.existsByIdCard(request.getIdCard())) {
            throw new DuplicateIdCardException(
                    "Ya existe un conductor con la cédula: " + request.getIdCard());
        }

        // 2. Categorías de licencias no repetidas dentro del request
        licenseService.validateNoDuplicateCategoriesInRequest(
                request.getLicenses(),
                item -> ((CreateDriverRequest.LicenseRequest) item).getCategory()
        );

        // 3. Vigencia máxima por categoría
        for (CreateDriverRequest.LicenseRequest lr : request.getLicenses()) {
            licenseService.validateExpirationDate(
                    request.getBirthDate(), lr.getCategory(),
                    lr.getIssueDate(), lr.getExpirationDate());
        }

        // Obtener subestado ACTIVO/ACTIVO del catálogo
        EmploymentSubstatus activeSubstatus = substatusRepository.findActiveSubstatus()
                .orElseThrow(() -> new IllegalStateException(
                        "Subestado ACTIVO no encontrado. Verifique los datos iniciales de la BD."));

        // Construir entidad Driver
        Driver driver = new Driver();
        driver.setSubstatus(activeSubstatus);
        driver.setIdCard(request.getIdCard());
        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setBirthDate(request.getBirthDate());
        driver.setPhone(request.getPhone());
        driver.setHiringDate(request.getHiringDate());
        // operational_status = DISPONIBLE por default (definido en la entidad)

        // Construir licencias
        for (CreateDriverRequest.LicenseRequest lr : request.getLicenses()) {
            License license = new License();
            license.setDriver(driver);
            license.setCategory(lr.getCategory());
            license.setIssueDate(lr.getIssueDate());
            license.setExpirationDate(lr.getExpirationDate());
            driver.getLicenses().add(license);
        }

        // Construir contacto de emergencia
        CreateDriverRequest.EmergencyContactRequest ecr = request.getEmergencyContact();
        EmergencyContact contact = new EmergencyContact();
        contact.setDriver(driver);
        contact.setContactName(ecr.getContactName());
        contact.setContactPhone(ecr.getContactPhone());
        contact.setRelationship(ecr.getRelationship());
        driver.getEmergencyContacts().add(contact);

        Driver saved = driverRepository.save(driver);
        auditService.logDriverInsert(saved.getIdDriver(), currentUser());

        return driverMapper.toResponse(saved);
    }

    // ── Actualizar datos personales ───────────────────────────────────────────

    // Actualiza nombre, apellido y teléfono
    @Transactional
    public DriverResponse updatePersonalData(UUID id, UpdateDriverPersonalRequest request) {
        Driver driver = findWithSubstatusOrThrow(id);
        String user = currentUser();

        if (!driver.getFirstName().equals(request.getFirstName())) {
            auditService.logDriverUpdate(id, "first_name",
                    driver.getFirstName(), request.getFirstName(), user);
            driver.setFirstName(request.getFirstName());
        }
        if (!driver.getLastName().equals(request.getLastName())) {
            auditService.logDriverUpdate(id, "last_name",
                    driver.getLastName(), request.getLastName(), user);
            driver.setLastName(request.getLastName());
        }
        if (!driver.getPhone().equals(request.getPhone())) {
            auditService.logDriverUpdate(id, "phone",
                    driver.getPhone(), request.getPhone(), user);
            driver.setPhone(request.getPhone());
        }

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    // ── Cambiar estado laboral ───────────────────────────────────────

    // Cambia el estado laboral del conductor al subestado indicado.
    @Transactional
    public DriverResponse updateStatus(UUID id, UpdateDriverStatusRequest request) {
        Driver driver = findWithSubstatusOrThrow(id);

        EmploymentSubstatus newSubstatus = substatusRepository.findById(request.getIdSubstatus())
                .orElseThrow(() -> new SubstatusNotFoundException(
                        "Subestado no encontrado con id: " + request.getIdSubstatus()));

        String newStatusName = newSubstatus.getEmploymentStatus().getStatusName();
        boolean isDeactivation = "INACTIVO".equals(newStatusName) || "RETIRADO".equals(newStatusName);

        // Bloquear si está en ruta y se intenta desactivar
        if (isDeactivation && driver.getOperationalStatus() == OperationalStatus.EN_RUTA) {
            throw new DriverInRouteException(
                    "No se puede cambiar el estado del conductor porque está actualmente EN RUTA. " +
                            "Espere a que devuelva el vehículo.");
        }

        String oldSubstatusName = driver.getSubstatus().getSubstatusName();
        String user = currentUser();

        // Registrar retirement_date si pasa a RETIRADO; limpiarla si vuelve a ACTIVO
        if ("RETIRADO".equals(newStatusName) && driver.getRetirementDate() == null) {
            driver.setRetirementDate(LocalDate.now());
            auditService.logDriverUpdate(id, "retirement_date",
                    null, driver.getRetirementDate().toString(), user);
        } else if ("ACTIVO".equals(newStatusName) && driver.getRetirementDate() != null) {
            auditService.logDriverUpdate(id, "retirement_date",
                    driver.getRetirementDate().toString(), null, user);
            driver.setRetirementDate(null);
        }

        driver.setSubstatus(newSubstatus);
        auditService.logDriverUpdate(id, "id_substatus",
                oldSubstatusName, newSubstatus.getSubstatusName(), user);

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    private Driver findWithSubstatusOrThrow(UUID id) {
        return driverRepository.findByIdWithSubstatus(id)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Conductor no encontrado con id: " + id));
    }

    private String currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return "system";
        return auth.getPrincipal().toString();
    }
}