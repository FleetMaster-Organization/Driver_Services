package com.services.driver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload para actualizar el estado laboral de un conductor")
public class UpdateDriverStatusRequest {

    @NotNull(message = "El subestado es obligatorio")
    @Schema(
            description = "UUID del subestado destino (ACTIVO, SUSPENDIDO, VACACIONES, INCAPACIDAD, DESPEDIDO, RENUNCIA)",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )
    private UUID idSubstatus;
}