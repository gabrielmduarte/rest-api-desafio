package br.com.elotech.register.service;

import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.exception.DocumentAlreadyExistException;
import br.com.elotech.register.exception.PersonNotFoundException;
import br.com.elotech.register.mapper.PersonMapper;
import br.com.elotech.register.repository.PersonRepository;
import br.com.elotech.register.request.PersonRequest;
import br.com.elotech.register.request.UpdatePersonRequest;
import br.com.elotech.register.response.PersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.elotech.register.specification.PersonSpecification.*;

@Service
public class PersonService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    public PersonService(PersonMapper personMapper, PersonRepository personRepository, ContactService contactService) {
        this.personMapper = personMapper;
        this.personRepository = personRepository;
    }

    public PersonResponse findOne(final Long id) {
        final PersonEntity personEntity = getPersonEntity(id);

        return personMapper.toResponse(personEntity);
    }

    public Page<PersonResponse> findAllByCriteria(final String name, final String document,
                                                  final Boolean active, final Pageable pageable) {
        final Specification<PersonEntity> specification = getSpecification(name, document, active);
        final Page<PersonEntity> all = personRepository.findAll(specification, pageable);
        return all.map(personMapper::toResponse);
    }

    public void create(final PersonRequest personRequest) {
        final PersonEntity entity = personMapper.toEntity(personRequest);

        String documentFormatted = getDocumentFormatted(personRequest.getDocument());
        final boolean documentExist = personRepository.existsByDocument(documentFormatted);

        if (documentExist) throw new DocumentAlreadyExistException();

        entity.setDocument(documentFormatted);
        entity.setActive(true);

        personRepository.save(entity);
    }


    public void updatePerson(final Long id, final UpdatePersonRequest personRequest) {
        final PersonEntity personEntity = getPersonEntity(id);
        updateAttributes(personEntity, personRequest);

        personRepository.save(personEntity);
    }

    public void delete(final Long id) {
        final PersonEntity personEntity = getPersonEntity(id);
        personEntity.setActive(false);
        String falseDocument = personEntity.getDocument().replaceAll(".$", "");
        personEntity.setDocument(falseDocument);

        personRepository.save(personEntity);
    }

    public PersonEntity getPersonEntity(final Long id) {
        return personRepository.findByIdAndActiveTrue(id)
                .orElseThrow(PersonNotFoundException::new);
    }

    private void updateAttributes(final PersonEntity personEntity,
                                  final UpdatePersonRequest personRequest) {

        personEntity.setName(personRequest.getName());
        personEntity.setDateOfBirth(personRequest.getDateOfBirth());
    }

    private String getDocumentFormatted(String document) {
        return document.replaceAll("\\.", "").replaceAll("-", "");
    }

    private Specification<PersonEntity> getSpecification(final String name,
                                                         final String document,
                                                         final Boolean active) {
        return (Specification<PersonEntity>) (root, query, criteriaBuilder) -> {
            final List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(findByName(name).toPredicate(root, query, criteriaBuilder));
            }

            if (document != null) {
                predicates.add(findByDocument(document).toPredicate(root, query, criteriaBuilder));
            }

            if (active != null) {
                predicates.add(isActive(active).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        };
    }
}