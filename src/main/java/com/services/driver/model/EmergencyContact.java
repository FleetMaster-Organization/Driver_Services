package com.services.driver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Contacto de emergencia de un conductor.
 *
 * El administrador puede actualizar nombre, teléfono y parentesco
 * directamente sobre este registro (PATCH /drivers/{id}/emergency-contact/{contactId})
 * sin necesidad de eliminar y volver a crear.
 */
@Entity
@Table(name = "emergency_contacts")
@Getter
@Setter
@NoArgsConstructor
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_contact", updatable = false, nullable = false)
    private UUID idContact;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_driver", nullable = false)
    private Driver driver;

    @Column(name = "contact_name", length = 100, nullable = false)
    private String contactName;

    @Column(name = "contact_phone", length = 15, nullable = false)
    private String contactPhone;

    @Column(name = "relationship", length = 45, nullable = false)
    private String relationship;
}