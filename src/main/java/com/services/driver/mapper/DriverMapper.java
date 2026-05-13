package com.services.driver.mapper;

import com.services.driver.dto.response.DriverResponse;
import com.services.driver.dto.response.DriverSummaryResponse;
import com.services.driver.model.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverMapper {

    private final LicenseMapper licenseMapper;
    private final EmergencyContactMapper emergencyContactMapper;

    public DriverResponse toResponse(Driver driver) {
        return DriverResponse.builder()
                .idDriver(driver.getIdDriver())
                .idCard(driver.getIdCard())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .birthDate(driver.getBirthDate())
                .phone(driver.getPhone())
                .hiringDate(driver.getHiringDate())
                .retirementDate(driver.getRetirementDate())
                // Estado laboral y subestado derivados del JOIN substatus → employmentStatus
                .employmentStatus(driver.getSubstatus().getEmploymentStatus().getStatusName())
                .employmentSubstatus(driver.getSubstatus().getSubstatusName())
                .operationalStatus(driver.getOperationalStatus())
                .licenses(licenseMapper.toResponseList(driver.getLicenses()))
                .emergencyContacts(emergencyContactMapper.toResponseList(driver.getEmergencyContacts()))
                .build();
    }

    public DriverSummaryResponse toSummaryResponse(Driver driver) {
        return DriverSummaryResponse.builder()
                .idDriver(driver.getIdDriver())
                .idCard(driver.getIdCard())
                .fullName(driver.getFirstName() + " " + driver.getLastName())
                .employmentStatus(driver.getSubstatus().getEmploymentStatus().getStatusName())
                .employmentSubstatus(driver.getSubstatus().getSubstatusName())
                .operationalStatus(driver.getOperationalStatus())
                .licenses(licenseMapper.toResponseList(driver.getLicenses()))
                .build();
    }
}