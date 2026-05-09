package com.services.driver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Conductor vinculado a la empresa.
 *
 * Estado laboral: manejado a través de {@link EmploymentSubstatus}.
 *   Un solo FK (id_substatus) trae consigo el subestado (motivo) y,
 *   mediante JOIN, el estado padre (ACTIVO / INACTIVO / RETIRADO).
 *
 * Estado operativo: campo ENUM propio {@link OperationalStatus}.
 *   DISPONIBLE al crear; EN_RUTA cuando el módulo de asignaciones lo indique.
 *
 * birth_date: requerido para calcular la vigencia máxima permitida
 *   de cada categoría de licencia según la normativa colombiana.
 */
@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_driver", updatable = false, nullable = false)
    private UUID idDriver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_substatus", nullable = false)
    private EmploymentSubstatus substatus;

    @Column(name = "id_card", length = 20, nullable = false, unique = true)
    private String idCard;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @Column(name = "hiring_date", nullable = false)
    private LocalDate hiringDate;

    @Column(name = "retirement_date")
    private LocalDate retirementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false, length = 20)
    private OperationalStatus operationalStatus = OperationalStatus.DISPONIBLE;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<License> licenses = new ArrayList<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();
}