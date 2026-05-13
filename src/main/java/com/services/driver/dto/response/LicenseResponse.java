package com.services.driver.dto.response;

import com.services.driver.model.LicenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Datos de una categoría de licencia de conducción")
public class LicenseResponse {

    @Schema(description = "ID de la licencia", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID idLicense;

    @Schema(description = "Categoría de la licencia", example = "B1")
    private LicenseCategory category;

    @Schema(description = "Fecha de expedición", example = "2020-03-10")
    private LocalDate issueDate;

    @Schema(description = "Fecha de vencimiento", example = "2030-03-10")
    private LocalDate expirationDate;

    /**
     * Estado legal calculado: VIGENTE | VENCIDA.
     * Derivado de expiration_date vs LocalDate.now().
     */
    @Schema(description = "Estado legal calculado: VIGENTE o VENCIDA", example = "VIGENTE")
    private String licenseStatus;

    /**
     * Días hasta el vencimiento. Negativo si ya venció.
     * El frontend usa este valor para asignar el color del indicador visual.
     */
    @Schema(description = "Días hasta el vencimiento (negativo si ya venció)", example = "1460")
    private long daysUntilExpiration;

    /** Indica si la categoría pertenece a servicio público (C1, C2, C3). */
    @Schema(description = "¿Es categoría de servicio público?", example = "false")
    private boolean publicService;
}