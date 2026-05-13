package com.services.driver.dto.response;

import com.services.driver.model.OperationalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Vista resumida del conductor para el listado del dashboard")
public class DriverSummaryResponse {

    @Schema(description = "ID del conductor")
    private UUID idDriver;

    @Schema(description = "Cédula del conductor", example = "1234567890")
    private String idCard;

    @Schema(description = "Nombre completo", example = "Carlos López")
    private String fullName;

    @Schema(description = "Estado laboral", example = "ACTIVO")
    private String employmentStatus;

    @Schema(description = "Subestado laboral", example = "ACTIVO")
    private String employmentSubstatus;

    @Schema(description = "Estado operativo", example = "DISPONIBLE")
    private OperationalStatus operationalStatus;

    @Schema(description = "Licencias con indicadores de vigencia")
    private List<LicenseResponse> licenses;
}