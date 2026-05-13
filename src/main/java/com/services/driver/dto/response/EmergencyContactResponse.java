package com.services.driver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Schema(description = "Contacto de emergencia del conductor")
public class EmergencyContactResponse {

    @Schema(description = "ID del contacto", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID idContact;

    @Schema(description = "Nombre del contacto", example = "María López")
    private String contactName;

    @Schema(description = "Teléfono del contacto", example = "3109876543")
    private String contactPhone;

    @Schema(description = "Parentesco con el conductor", example = "Madre")
    private String relationship;
}