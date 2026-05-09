package com.services.driver.model;

/**
 * Estado operativo del conductor.
 *
 * DISPONIBLE → puede ser seleccionado para una asignación.
 * EN_RUTA    → tiene un vehículo activo asignado; no puede inactivarse laboralmente.
 */
public enum OperationalStatus {
    DISPONIBLE,
    EN_RUTA
}