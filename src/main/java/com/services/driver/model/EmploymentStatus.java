package com.services.driver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "employment_status")
@Getter
@Setter
@NoArgsConstructor
public class EmploymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_status", updatable = false, nullable = false)
    private UUID idStatus;

    @Column(name = "status_name", length = 20, nullable = false)
    private String statusName;
}