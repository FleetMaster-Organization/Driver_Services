package com.services.driver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos actualizables del contacto de emergencia")
public class UpdateEmergencyContactRequest {

    @NotBlank(message = "El nombre del contacto es obligatorio")
    @Size(max = 100, message = "El nombre del contacto no puede superar 100 caracteres")
    @Schema(description = "Nombre completo del contacto de emergencia", example = "María López")
    private String contactName;

    @NotBlank(message = "El teléfono del contacto es obligatorio")
    @Size(max = 15, message = "El teléfono no puede superar 15 caracteres")
    @Schema(description = "Teléfono del contacto de emergencia", example = "3109876543")
    private String contactPhone;

    @NotBlank(message = "El parentesco es obligatorio")
    @Size(max = 45, message = "El parentesco no puede superar 45 caracteres")
    @Schema(description = "Relación con el conductor", example = "Madre")
    private String relationship;
}