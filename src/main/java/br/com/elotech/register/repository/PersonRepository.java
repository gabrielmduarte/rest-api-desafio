package br.com.elotech.register.repository;

import br.com.elotech.register.domain.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long>,
                                            JpaSpecificationExecutor<PersonEntity> {

    Optional<PersonEntity> findByIdAndActiveTrue(final Long id);

    boolean existsByDocument(final String document);

}
