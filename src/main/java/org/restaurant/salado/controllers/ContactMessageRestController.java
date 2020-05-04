package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.ContactMessage;
import org.restaurant.salado.models.ContactMessageResponse;
import org.restaurant.salado.services.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/contactmessages")
public class ContactMessageRestController {

    private ContactMessageService contactMessageService;

    @Autowired
    public void setContactMessageService(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }


    /**
     * Search ContactMessage objects
     *
     * @param search: Search criteria
     * @param page:   Requested Page
     * @param size:   Requested Page Size
     * @return ResponseEntity<Page < ContactMessage>>
     */
    @GetMapping(path = "/")
    public ResponseEntity<Page<ContactMessage>> searchContactMessages(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.contactMessageService.getContactMessages(search, page, size));
    }

    /**
     * Respond to User ContactMessage
     *
     * @param id:       ContactMessage Object ID
     * @param response: Response Message
     * @return ResponseEntity<ContactMessage>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/{id}/respond")
    public ResponseEntity<ContactMessage> respondContactMessage(@PathVariable(name = "id") Long id, @RequestBody ContactMessageResponse response) {
        return ResponseEntity.ok(this.contactMessageService.respondContactMessage(id, response.getResponse()));
    }

}
