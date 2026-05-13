package com.services.driver.controller;

import com.services.driver.dto.request.AddLicenseRequest;
import com.services.driver.dto.request.UpdateLicenseExpirationRequest;
import com.services.driver.dto.response.LicenseResponse;
import com.services.driver.service.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/drivers/{driverId}/licenses")
@RequiredArgsConstructor
@Tag(name = "Licencias", description = "Gestión de categorías de licencia por conductor")
public class LicenseController {

    private final LicenseService licenseService;

    // ── GET /drivers/{driverId}/licenses ───────────────────────────────

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_COORDINADOR_FLOTA')")
    @Operation(summary = "Listar licencias del conductor",
            description = "Devuelve todas las categorías registradas con su estado legal calculado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Licencias obtenidas"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public ResponseEntity<List<LicenseResponse>> getLicenses(@PathVariable UUID driverId) {
        return ResponseEntity.ok(licenseService.getLicensesByDriver(driverId));
    }

    // ── POST /drivers/{driverId}/licenses ──────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Agregar categoría de licencia",
            description = "Agrega una nueva categoría al conductor. No puede repetirse una categoría ya registrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Licencia agregada"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Categoría ya registrada para este conductor"),
            @ApiResponse(responseCode = "422", description = "Vigencia excede el máximo permitido por normativa")
    })
    public ResponseEntity<LicenseResponse> addLicense(
            @PathVariable UUID driverId,
            @Valid @RequestBody AddLicenseRequest request) {
        LicenseResponse response = licenseService.addLicense(driverId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getIdLicense())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    // ── PATCH /drivers/{driverId}/licenses/{licenseId}/expiration ──────

    @PatchMapping("/{licenseId}/expiration")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Renovar vencimiento de licencia",
            description = "Actualiza la fecha de vencimiento y recalcula automáticamente el estado legal (REQ-18).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vencimiento actualizado"),
            @ApiResponse(responseCode = "404", description = "Conductor o licencia no encontrada"),
            @ApiResponse(responseCode = "422", description = "Vigencia excede el máximo permitido por normativa")
    })
    public ResponseEntity<LicenseResponse> updateExpiration(
            @PathVariable UUID driverId,
            @PathVariable UUID licenseId,
            @Valid @RequestBody UpdateLicenseExpirationRequest request) {
        return ResponseEntity.ok(licenseService.updateExpiration(driverId, licenseId, request));
    }
}