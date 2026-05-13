package com.services.driver.dto.request;

import com.services.driver.model.LicenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para registrar un nuevo conductor")
public class CreateDriverRequest {

    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 20, message = "La cédula no puede superar 20 caracteres")
    @Schema(description = "Número de cédula", example = "1234567890")
    private String idCard;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(description = "Nombre del conductor", example = "Carlos")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede superar 50 caracteres")
    @Schema(description = "Apellido del conductor", example = "López")
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Schema(description = "Fecha de nacimiento (yyyy-MM-dd). El conductor debe tener al menos 18 años.", example = "1990-05-15")
    private LocalDate birthDate;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 10, message = "El teléfono no puede superar 10 caracteres")
    @Schema(description = "Teléfono de contacto", example = "3001234567")
    private String phone;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @PastOrPresent(message = "La fecha de contratación no puede ser futura")
    @Schema(description = "Fecha de contratación (yyyy-MM-dd)", example = "2024-01-15")
    private LocalDate hiringDate;

    /**
     * Al menos una categoría de licencia es obligatoria
     * El Service valida que no haya categorías repetidas dentro de la lista.
     */
    @NotEmpty(message = "Se requiere al menos una categoría de licencia")
    @Valid
    @Schema(description = "Categorías de licencia del conductor. Mínimo una requerida.")
    private List<LicenseRequest> licenses;

    /** Contacto de emergencia obligatorio */
    @NotNull(message = "El contacto de emergencia es obligatorio")
    @Valid
    @Schema(description = "Contacto de emergencia del conductor")
    private EmergencyContactRequest emergencyContact;

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "Datos de una categoría de licencia")
    public static class LicenseRequest {

        @NotNull(message = "La categoría de licencia es obligatoria")
        @Schema(description = "Categoría (A1, A2, B1, B2, B3, C1, C2, C3)", example = "B1")
        private LicenseCategory category;

        @NotNull(message = "La fecha de expedición es obligatoria")
        @PastOrPresent(message = "La fecha de expedición no puede ser futura")
        @Schema(description = "Fecha de expedición de la licencia (yyyy-MM-dd)", example = "2020-03-10")
        private LocalDate issueDate;

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        @Future(message = "La fecha de vencimiento debe ser futura al registrar")
        @Schema(description = "Fecha de vencimiento de la licencia (yyyy-MM-dd)", example = "2030-03-10")
        private LocalDate expirationDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "Datos del contacto de emergencia")
    public static class EmergencyContactRequest {

        @NotBlank(message = "El nombre del contacto es obligatorio")
        @Size(max = 100, message = "El nombre del contacto no puede superar 100 caracteres")
        @Schema(description = "Nombre completo del contacto", example = "María López")
        private String contactName;

        @NotBlank(message = "El teléfono del contacto es obligatorio")
        @Size(max = 10, message = "El teléfono del contacto no puede superar 10 caracteres")
        @Schema(description = "Teléfono del contacto", example = "3109876543")
        private String contactPhone;

        @NotBlank(message = "El parentesco es obligatorio")
        @Size(max = 45, message = "El parentesco no puede superar 45 caracteres")
        @Schema(description = "Relación con el conductor", example = "Madre")
        private String relationship;
    }
}