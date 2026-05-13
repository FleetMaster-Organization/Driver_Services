package com.services.driver.controller;

import com.services.driver.dto.request.CreateDriverRequest;
import com.services.driver.dto.request.UpdateDriverPersonalRequest;
import com.services.driver.dto.request.UpdateDriverStatusRequest;
import com.services.driver.dto.response.DriverResponse;
import com.services.driver.dto.response.DriverSummaryResponse;
import com.services.driver.dto.response.SubstatusResponse;
import com.services.driver.service.DriverService;
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
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Conductores", description = "Gestión del personal operativo")
public class DriverController {

    private final DriverService driverService;

    // ── GET /drivers ───────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_COORDINADOR_FLOTA')")
    @Operation(summary = "Listar conductores",
            description = "Devuelve el listado completo con indicadores de vigencia de licencia. " +
                    "El frontend determina el color (verde/amarillo/rojo) con daysUntilExpiration.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<DriverSummaryResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // ── GET /drivers/{id} ──────────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_COORDINADOR_FLOTA')")
    @Operation(summary = "Obtener conductor por ID",
            description = "Devuelve el perfil completo con licencias, estado laboral y contacto de emergencia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable UUID id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    // ── GET /drivers/substatuses ──────────────────────────────────────

    @GetMapping("/substatuses")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Listar subestados disponibles",
            description = "Devuelve todos los subestados laborales con su estado padre. " +
                    "Usado para popular el selector al cambiar el estado de un conductor.")
    public ResponseEntity<List<SubstatusResponse>> getAllSubstatuses() {
        return ResponseEntity.ok(driverService.getAllSubstatuses());
    }

    // ── POST /drivers ──────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Registrar conductor",
            description = "Crea el perfil con estado ACTIVO/DISPONIBLE automático. " +
                    "Requiere al menos una licencia y un contacto de emergencia.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conductor registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Cédula duplicada o categoría repetida"),
            @ApiResponse(responseCode = "422", description = "Vigencia de licencia excede el máximo permitido")
    })
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody CreateDriverRequest request) {
        DriverResponse response = driverService.createDriver(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getIdDriver())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    // ── PATCH /drivers/{id}/personal ──────────────────────────────────

    @PatchMapping("/{id}/personal")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Actualizar datos personales",
            description = "Actualiza nombre, apellido y teléfono. La cédula y fecha de nacimiento no son modificables.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos actualizados"),
            @ApiResponse(responseCode = "404", description = "Conductor no encontrado")
    })
    public ResponseEntity<DriverResponse> updatePersonalData(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDriverPersonalRequest request) {
        return ResponseEntity.ok(driverService.updatePersonalData(id, request));
    }

    // ── PATCH /drivers/{id}/status ────────────────────────────────────

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(summary = "Cambiar estado laboral",
            description = "Cambia el estado laboral y subestado del conductor. " +
                    "Bloqueado si el conductor está EN_RUTA al intentar INACTIVO o RETIRADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Conductor o subestado no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conductor EN_RUTA, no se puede inactivar")
    })
    public ResponseEntity<DriverResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDriverStatusRequest request) {
        return ResponseEntity.ok(driverService.updateStatus(id, request));
    }
}