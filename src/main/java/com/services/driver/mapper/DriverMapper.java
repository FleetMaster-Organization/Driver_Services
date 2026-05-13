package com.services.driver.mapper;

import com.services.driver.dto.response.DriverResponse;
import com.services.driver.dto.response.DriverSummaryResponse;
import com.services.driver.model.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Convierte entidades Driver a DTOs de respuesta.
 *
 * Delega la conversión de licencias y contactos a sus mappers específicos.
 * Se invoca siempre dentro de un método @Transactional del Service,
 * donde la sesión Hibernate está abierta y las relaciones LAZY son accesibles.
 */
@Component
@RequiredArgsConstructor
public class DriverMapper {

    private final LicenseMapper licenseMapper;
    private final EmergencyContactMapper emergencyContactMapper;

    /**
     * Respuesta completa: incluye todos los datos del conductor,
     * sus licencias con estado legal calculado y sus contactos de emergencia.
     * Usada en GET /api/v1/drivers/{id}
     */
    public DriverResponse toResponse(Driver driver) {
        return DriverResponse.builder()
                .idDriver(driver.getIdDriver())
                .idCard(driver.getIdCard())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .birthDate(driver.getBirthDate())
                .phone(driver.getPhone())
                .hiringDate(driver.getHiringDate())
                .retirementDate(driver.getRetirementDate())
                // Estado laboral y subestado derivados del JOIN substatus → employmentStatus
                .employmentStatus(driver.getSubstatus().getEmploymentStatus().getStatusName())
                .employmentSubstatus(driver.getSubstatus().getSubstatusName())
                .operationalStatus(driver.getOperationalStatus())
                .licenses(licenseMapper.toResponseList(driver.getLicenses()))
                .emergencyContacts(emergencyContactMapper.toResponseList(driver.getEmergencyContacts()))
                .build();
    }

    /**
     * Respuesta resumida para el listado del dashboard.
     * Usada en GET /api/v1/drivers
     * No incluye contactos de emergencia para reducir el payload.
     */
    public DriverSummaryResponse toSummaryResponse(Driver driver) {
        return DriverSummaryResponse.builder()
                .idDriver(driver.getIdDriver())
                .idCard(driver.getIdCard())
                .fullName(driver.getFirstName() + " " + driver.getLastName())
                .employmentStatus(driver.getSubstatus().getEmploymentStatus().getStatusName())
                .employmentSubstatus(driver.getSubstatus().getSubstatusName())
                .operationalStatus(driver.getOperationalStatus())
                .licenses(licenseMapper.toResponseList(driver.getLicenses()))
                .build();
    }
}