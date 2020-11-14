package br.com.elotech.register.specification;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.domain.PersonEntity;
import org.springframework.data.jpa.domain.Specification;

public class ContactSpecification {

    public static Specification<ContactEntity> findByPhone(final String phone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<ContactEntity> findByEmail(final String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<ContactEntity> findByName(final String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<PersonEntity> byId(final Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

}
