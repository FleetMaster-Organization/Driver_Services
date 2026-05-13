package com.services.driver.service;

import com.services.driver.dto.request.UpdateEmergencyContactRequest;
import com.services.driver.dto.response.EmergencyContactResponse;
import com.services.driver.exception.DriverNotFoundException;
import com.services.driver.mapper.EmergencyContactMapper;
import com.services.driver.model.EmergencyContact;
import com.services.driver.repository.EmergencyContactRepository;
import com.services.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository contactRepository;
    private final DriverRepository driverRepository;
    private final EmergencyContactMapper contactMapper;
    private final AuditService auditService;

    // Actualiza nombre, teléfono y/o parentesco del contacto de emergencia.
    @Transactional
    public EmergencyContactResponse updateContact(UUID driverId, UUID contactId,
                                                  UpdateEmergencyContactRequest request) {
        validateDriverExists(driverId);

        EmergencyContact contact = contactRepository
                .findByIdContactAndDriver_IdDriver(contactId, driverId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Contacto de emergencia no encontrado para el conductor indicado"));

        String user = currentUser();

        if (!contact.getContactName().equals(request.getContactName())) {
            auditService.logContactUpdate(contactId, "contact_name",
                    contact.getContactName(), request.getContactName(), user);
            contact.setContactName(request.getContactName());
        }
        if (!contact.getContactPhone().equals(request.getContactPhone())) {
            auditService.logContactUpdate(contactId, "contact_phone",
                    contact.getContactPhone(), request.getContactPhone(), user);
            contact.setContactPhone(request.getContactPhone());
        }
        if (!contact.getRelationship().equals(request.getRelationship())) {
            auditService.logContactUpdate(contactId, "relationship",
                    contact.getRelationship(), request.getRelationship(), user);
            contact.setRelationship(request.getRelationship());
        }

        return contactMapper.toResponse(contactRepository.save(contact));
    }

    private void validateDriverExists(UUID driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException("Conductor no encontrado con id: " + driverId);
        }
    }

    private String currentUser() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}