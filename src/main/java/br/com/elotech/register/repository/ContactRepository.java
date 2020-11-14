package br.com.elotech.register.repository;

import br.com.elotech.register.domain.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long>,
                                             JpaSpecificationExecutor<ContactEntity> {
}
