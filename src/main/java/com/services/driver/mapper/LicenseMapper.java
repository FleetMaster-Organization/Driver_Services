package com.services.driver.mapper;

import com.services.driver.dto.response.LicenseResponse;
import com.services.driver.model.License;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Convierte entidades License a DTOs de respuesta.
 *
 * Responsabilidad exclusiva: transformación de datos.
 * Aquí vive el cálculo de licenseStatus y daysUntilExpiration porque
 * son derivados directos de los datos de la entidad, no lógica de negocio.
 * La lógica de negocio (validar vigencia máxima, rechazar duplicados) vive en LicenseService.
 */
@Component
public class LicenseMapper {

    public LicenseResponse toResponse(License license) {
        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, license.getExpirationDate());
        String status = days >= 0 ? "VIGENTE" : "VENCIDA";

        return LicenseResponse.builder()
                .idLicense(license.getIdLicense())
                .category(license.getCategory())
                .issueDate(license.getIssueDate())
                .expirationDate(license.getExpirationDate())
                .licenseStatus(status)
                .daysUntilExpiration(days)
                .publicService(license.getCategory().isPublicService())
                .build();
    }

    public List<LicenseResponse> toResponseList(List<License> licenses) {
        return licenses.stream()
                .map(this::toResponse)
                .toList();
    }
}