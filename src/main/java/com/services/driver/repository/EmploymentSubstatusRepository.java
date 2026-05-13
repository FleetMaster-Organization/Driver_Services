package com.services.driver.repository;

import com.services.driver.model.EmploymentSubstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmploymentSubstatusRepository extends JpaRepository<EmploymentSubstatus, UUID> {


    // Obtiene el subestado ACTIVO/ACTIVO para asignarlo al crear un conductor.
    @Query("""
            SELECT sub FROM EmploymentSubstatus sub
            JOIN sub.employmentStatus es
            WHERE es.statusName = 'ACTIVO' AND sub.substatusName = 'ACTIVO'
            """)
    Optional<EmploymentSubstatus> findActiveSubstatus();

    // Lista todos los subestados con su estado padre cargado.
    @Query("""
            SELECT sub FROM EmploymentSubstatus sub
            JOIN FETCH sub.employmentStatus
            ORDER BY sub.employmentStatus.statusName, sub.substatusName
            """)
    java.util.List<EmploymentSubstatus> findAllWithStatus();
}