package com.services.driver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Subestados laborales asociados a cada estado padre.
 *
 * ACTIVO   → ACTIVO
 * INACTIVO → SUSPENDIDO | VACACIONES | INCAPACIDAD
 * RETIRADO → DESPEDIDO  | RENUNCIA
 *
 * Al guardar un Driver solo se persiste id_substatus; el estado padre
 * (employment_status) se obtiene mediante JOIN en las consultas.
 */
@Entity
@Table(name = "employment_substatus")
@Getter
@Setter
@NoArgsConstructor
public class EmploymentSubstatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_substatus", updatable = false, nullable = false)
    private UUID idSubstatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(name = "substatus_name", length = 20, nullable = false)
    private String substatusName;
}