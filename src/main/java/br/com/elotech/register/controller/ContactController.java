package br.com.elotech.register.controller;

import br.com.elotech.register.request.ContactRequest;
import br.com.elotech.register.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/person")
public class ContactController {

    final private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/contact")
    public void create(@PathVariable final Long id,
                       @Valid @RequestBody ContactRequest contactRequest) {
        log.info("M=create, I=creating one more contact for a person list, Person id={}",id);
        contactService.create(id, contactRequest);
    }

    @PutMapping("/{personId}/contact/{contactId}")
    public void update(@PathVariable final Long personId,
                       @PathVariable final Long contactId,
                       @Valid @RequestBody ContactRequest contactRequest) {
        log.info("M=update, I=updating contact, Person id={}, Contact id={}",personId, contactId);
        contactService.update(personId, contactId, contactRequest);
    }

    @DeleteMapping("/{personId}/contact/{contactId}")
    public void update(@PathVariable final Long personId,
                       @PathVariable final Long contactId) {
        log.info("M=delete, I=deleting contact, Person id={}, Contact id={}",personId, contactId);
        contactService.delete(personId, contactId);
    }

}

