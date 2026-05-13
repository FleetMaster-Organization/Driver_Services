package com.services.driver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Nueva fecha de vencimiento para renovar una licencia")
public class UpdateLicenseExpirationRequest {

    @NotNull(message = "La nueva fecha de vencimiento es obligatoria")
    @Future(message = "La nueva fecha de vencimiento debe ser futura")
    @Schema(description = "Nueva fecha de vencimiento (yyyy-MM-dd)", example = "2032-03-10")
    private LocalDate expirationDate;
}