package com.services.driver.model.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Registro de auditoría para cambios sobre la entidad Driver.
 */
@Entity
@Table(name = "drivers_audits")
@Getter
@Setter
@NoArgsConstructor
public class DriverAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit")
    private Long idAudit;

    @Column(name = "id_driver", nullable = false)
    private UUID idDriver;

    @Column(name = "action_type", length = 20, nullable = false)
    private String actionType;

    @Column(name = "modified_field", length = 50)
    private String modifiedField;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    /** Subject del JWT (UUID del usuario que realizó la acción). */
    @Column(name = "modified_by", length = 100, nullable = false)
    private String modifiedBy;

    @Column(name = "modified_at", nullable = false)
    private OffsetDateTime modifiedAt = OffsetDateTime.now();
}