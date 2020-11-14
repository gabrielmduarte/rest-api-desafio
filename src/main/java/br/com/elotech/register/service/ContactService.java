package br.com.elotech.register.service;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.exception.ContactNotFoundException;
import br.com.elotech.register.exception.ContactNotFoundInPersonListException;
import br.com.elotech.register.exception.ListOfContactCantBeEmptyException;
import br.com.elotech.register.exception.PersonNotFoundException;
import br.com.elotech.register.mapper.ContactMapper;
import br.com.elotech.register.repository.ContactRepository;
import br.com.elotech.register.repository.PersonRepository;
import br.com.elotech.register.request.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    public void create(Long id, ContactRequest contactRequest) {
        PersonEntity personEntity = personService.getPersonEntity(id);

        ContactEntity contactEntity = contactMapper.toEntity(contactRequest);

        personEntity.getContacts().add(contactEntity);

        personRepository.save(personEntity);
    }

    public void update(Long personId, Long contactId, ContactRequest contactRequest) {
        PersonEntity personEntity = personService.getPersonEntity(personId);
        ContactEntity contactEntity = contactRepository.findById(contactId).orElseThrow(ContactNotFoundException::new);

        if (personEntity.getContacts().contains(contactEntity)) {
            contactEntity.setEmail(contactRequest.getEmail());
            contactEntity.setName(contactRequest.getName());
            contactEntity.setPhone(contactRequest.getPhone());

            contactRepository.save(contactEntity);
        } else throw new ContactNotFoundInPersonListException();
    }

    @Transactional
    public void delete(Long personId, Long contactId) {
        ContactEntity contactEntity = contactRepository.findById(contactId)
                                                        .orElseThrow(ContactNotFoundException::new);
        PersonEntity person = personRepository.findByIdAndActiveTrue(personId)
                                                .orElseThrow(PersonNotFoundException::new);

        if (person.getContacts().size() == 1) {
            throw new ListOfContactCantBeEmptyException();
        }

        if (!person.getContacts().contains(contactEntity)) {
            throw new ContactNotFoundInPersonListException();
        }

        person.getContacts().remove(contactEntity);
        personRepository.save(person);
    }

}



