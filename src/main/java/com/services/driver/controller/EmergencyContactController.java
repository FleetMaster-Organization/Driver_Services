package com.services.driver.controller;

import com.services.driver.dto.request.UpdateEmergencyContactRequest;
import com.services.driver.dto.response.EmergencyContactResponse;
import com.services.driver.service.EmergencyContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/drivers/{driverId}/emergency-contact")
@RequiredArgsConstructor
@Tag(name = "Contacto de emergencia", description = "Gestión del contacto de emergencia del conductor")
public class EmergencyContactController {

    private final EmergencyContactService contactService;

    // ── PATCH /drivers/{driverId}/emergency-contact/{contactId} ────────

    @PatchMapping("/{contactId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Actualizar contacto de emergencia",
            description = "Modifica nombre, teléfono y/o parentesco sobre el registro existente. " +
                    "No elimina ni recrea el contacto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacto actualizado"),
            @ApiResponse(responseCode = "404", description = "Conductor o contacto no encontrado")
    })
    public ResponseEntity<EmergencyContactResponse> updateContact(
            @PathVariable UUID driverId,
            @PathVariable UUID contactId,
            @Valid @RequestBody UpdateEmergencyContactRequest request) {
        return ResponseEntity.ok(contactService.updateContact(driverId, contactId, request));
    }
}