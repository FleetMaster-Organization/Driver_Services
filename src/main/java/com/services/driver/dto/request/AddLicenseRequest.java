package com.services.driver.dto.request;

import com.services.driver.model.LicenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para agregar una categoría de licencia a un conductor")
public class AddLicenseRequest {

    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "Categoría de licencia colombiana", example = "C1")
    private LicenseCategory category;

    @NotNull(message = "La fecha de expedición es obligatoria")
    @PastOrPresent(message = "La fecha de expedición no puede ser futura")
    @Schema(description = "Fecha de expedición (yyyy-MM-dd)", example = "2022-06-01")
    private LocalDate issueDate;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La fecha de vencimiento debe ser futura al registrar")
    @Schema(description = "Fecha de vencimiento (yyyy-MM-dd)", example = "2027-06-01")
    private LocalDate expirationDate;
}