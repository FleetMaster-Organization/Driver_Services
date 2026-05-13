package com.services.driver.repository;

import com.services.driver.model.License;
import com.services.driver.model.LicenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LicenseRepository extends JpaRepository<License, UUID> {

    // Previene la duplicidad de categorias de licencias ya registradas.
    boolean existsByDriver_IdDriverAndCategory(UUID driverId, LicenseCategory category);

     //Obtiene todas las licencias de un conductor ordenadas por categoría.
     //Usado al construir el DriverResponse con sus licencias y estado legal calculado.
    @Query("SELECT l FROM License l WHERE l.driver.idDriver = :driverId ORDER BY l.category")
    List<License> findByDriverId(UUID driverId);
}