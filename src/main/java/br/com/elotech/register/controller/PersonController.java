package br.com.elotech.register.controller;

import br.com.elotech.register.request.PersonRequest;
import br.com.elotech.register.request.UpdatePersonRequest;
import br.com.elotech.register.response.PersonResponse;
import br.com.elotech.register.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public PersonResponse findOne(@PathVariable final Long id) {
        log.info("M=findOne, I=Finding one person by id, id={}", id);
        return personService.findOne(id);
    }

    @GetMapping
    public Page<PersonResponse> findAll(@RequestParam(value = "name", required = false) final String name,
                                        @RequestParam(value = "document", required = false) final String document,
                                        @RequestParam(value = "active", required = false, defaultValue = "true")
                                            final Boolean active,
                                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                            final Pageable pageable) {
        return personService.findAllByCriteria(name, document, active, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody final PersonRequest personRequest) {
        log.info("M=create, I=Creating Person, document={}", personRequest.getDocument());
        personService.create(personRequest);
    }

    @PutMapping("/{id}")
    public void updatePerson(@PathVariable final Long id,
                             @Valid @RequestBody final UpdatePersonRequest personRequest) {
        log.info("M=updateAllAttributes, I=Updating one person by id, id={}", id);
        personService.updatePerson(id, personRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        log.info("M=delete, I=Deleting one person by id, id={}", id);
        personService.delete(id);
    }

}
