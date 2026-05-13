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

    /** Verifica si ya existe un conductor con esa cédula (REQ-19: no duplicar). */
    boolean existsByIdCard(String idCard);

    /** Busca por cédula; útil para validar duplicidad al editar. */
    Optional<Driver> findByIdCard(String idCard);

    /**
     * Carga el conductor con su subestado y estado padre en una sola query.
     * Evita N+1 al acceder a substatus.employmentStatus.
     */
    @Query("""
            SELECT d FROM Driver d
            JOIN FETCH d.substatus sub
            JOIN FETCH sub.employmentStatus
            WHERE d.idDriver = :id
            """)
    Optional<Driver> findByIdWithSubstatus(UUID id);

    /**
     * Lista todos los conductores con sus relaciones de estado en una sola query.
     * Usado para el listado del dashboard (REQ-20).
     */
    @Query("""
            SELECT d FROM Driver d
            JOIN FETCH d.substatus sub
            JOIN FETCH sub.employmentStatus
            ORDER BY d.lastName, d.firstName
            """)
    List<Driver> findAllWithSubstatus();
}