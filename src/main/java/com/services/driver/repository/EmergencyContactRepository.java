package com.services.driver.repository;

import com.services.driver.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {

     // Busca un contacto específico de un conductor.
     // Usado al actualizar para verificar que el contacto pertenece al driver correcto.
    Optional<EmergencyContact> findByIdContactAndDriver_IdDriver(UUID idContact, UUID driverId);
}