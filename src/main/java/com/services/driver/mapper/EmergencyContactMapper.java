package com.services.driver.mapper;

import com.services.driver.dto.response.EmergencyContactResponse;
import com.services.driver.model.EmergencyContact;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class EmergencyContactMapper {

    public EmergencyContactResponse toResponse(EmergencyContact contact) {
        return EmergencyContactResponse.builder()
                .idContact(contact.getIdContact())
                .contactName(contact.getContactName())
                .contactPhone(contact.getContactPhone())
                .relationship(contact.getRelationship())
                .build();
    }

    public List<EmergencyContactResponse> toResponseList(Collection<EmergencyContact> contacts) {
        return contacts.stream()
                .map(this::toResponse)
                .toList();
    }
}