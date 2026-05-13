package com.services.driver.model;


 //DISPONIBLE → puede ser seleccionado para una asignación.
 //EN_RUTA    → tiene un vehiculo activo asignado, no puede inactivarse laboralmente.
public enum OperationalStatus {
    DISPONIBLE,
    EN_RUTA
}