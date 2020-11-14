package br.com.elotech.register.specification;

import br.com.elotech.register.domain.PersonEntity;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<PersonEntity> findByDocument(final String document) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("document"), "%" + document + "%");
    }

    public static Specification<PersonEntity> findByName(final String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<PersonEntity> isActive(final boolean active) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), active);
    }
}
