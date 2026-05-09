package com.services.driver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Licencia de conducción de un conductor.
 *
 * Cada registro representa UNA categoría (A1, A2, B1...).
 * Un conductor puede tener múltiples categorías, pero no puede repetir
 * la misma (constraint uq_driver_category en la tabla licenses).
 *
 * Estado legal (VIGENTE / VENCIDA) NO se persiste: se calcula dinámicamente
 * comparando expiration_date con LocalDate.now() en la capa de servicio.
 *
 * La vigencia máxima permitida depende de la categoría y la edad del conductor
 * en la fecha de expedición (ver LicenseCategory.maxValidityYears).
 */
@Entity
@Table(
        name = "licenses",
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_driver_category", columnNames = {"id_driver", "category"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_license", updatable = false, nullable = false)
    private UUID idLicense;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_driver", nullable = false)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 3)
    private LicenseCategory category;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
}