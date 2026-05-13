package com.services.driver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Schema(description = "Subestado laboral disponible para asignar a un conductor")
public class SubstatusResponse {

    @Schema(description = "ID del subestado (se envía en UpdateDriverStatusRequest)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID idSubstatus;

    @Schema(description = "Estado padre", example = "INACTIVO")
    private String statusName;

    @Schema(description = "Nombre del subestado", example = "VACACIONES")
    private String substatusName;
}