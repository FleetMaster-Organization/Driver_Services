package com.services.driver.repository;

import com.services.driver.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

    // Verifica si existe un conductor con determinada cédula y evita duplicidad.
    boolean existsByIdCard(String idCard);

    // Carga un conductor con todas sus relaciones en una sola query.
    // Carga: substatus → employmentStatus, licenses, emergencyContacts.
    @Query("""
            SELECT DISTINCT d FROM Driver d
            JOIN FETCH d.substatus sub
            JOIN FETCH sub.employmentStatus
            LEFT JOIN FETCH d.licenses
            LEFT JOIN FETCH d.emergencyContacts
            WHERE d.idDriver = :id
            """)
    Optional<Driver> findByIdWithSubstatus(UUID id);

    //  Lista todos los conductores con estado y licencias
    @Query("""
            SELECT DISTINCT d FROM Driver d
            JOIN FETCH d.substatus sub
            JOIN FETCH sub.employmentStatus
            LEFT JOIN FETCH d.licenses
            ORDER BY d.lastName, d.firstName
            """)
    List<Driver> findAllWithSubstatus();
}