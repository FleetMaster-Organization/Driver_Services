package com.services.driver.dto.response;

import com.services.driver.model.OperationalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Perfil completo de un conductor")
public class DriverResponse {

    @Schema(description = "ID del conductor", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID idDriver;

    @Schema(description = "Número de cédula", example = "1234567890")
    private String idCard;

    @Schema(description = "Nombre", example = "Carlos")
    private String firstName;

    @Schema(description = "Apellido", example = "López")
    private String lastName;

    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate birthDate;

    @Schema(description = "Teléfono", example = "3001234567")
    private String phone;

    @Schema(description = "Fecha de contratación", example = "2024-01-15")
    private LocalDate hiringDate;

    @Schema(description = "Fecha de retiro (null si activo/inactivo)", example = "null")
    private LocalDate retirementDate;

    @Schema(description = "Estado laboral del conductor", example = "INACTIVO")
    private String employmentStatus;

    @Schema(description = "Subestado / motivo del estado laboral", example = "VACACIONES")
    private String employmentSubstatus;

    @Schema(description = "Estado operativo", example = "DISPONIBLE")
    private OperationalStatus operationalStatus;

    @Schema(description = "Licencias del conductor con estado legal calculado")
    private List<LicenseResponse> licenses;

    @Schema(description = "Contactos de emergencia del conductor")
    private List<EmergencyContactResponse> emergencyContacts;
}