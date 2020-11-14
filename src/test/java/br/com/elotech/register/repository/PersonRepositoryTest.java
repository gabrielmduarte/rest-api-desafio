package br.com.elotech.register.repository;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.exception.PersonNotFoundException;
import com.sun.tools.javac.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    public void createShouldPersistPerson() {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setPhone("11998877664");
        contactEntity.setName("Lewis Hamilton");
        contactEntity.setEmail("lewis@gmail.com");

        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Teste");
        personEntity.setDocument("89169744025");
        personEntity.setDateOfBirth(LocalDate.of(2000,05,05));
        personEntity.setContacts(List.of(contactEntity));
        personEntity.setActive(true);

        repository.save(personEntity);

        Assertions.assertThat(personEntity.getId()).isNotNull();
        Assertions.assertThat(personEntity.getName()).isEqualTo("Teste");
        Assertions.assertThat(personEntity.getDocument()).isEqualTo("89169744025");
        Assertions.assertThat(personEntity.getDateOfBirth()).isEqualTo(LocalDate.of(2000,05,05));
        Assertions.assertThat(personEntity.getContacts().size()).isEqualTo(1);
    }

    @Test
    public void mustFindThePersonWhenSearchedForTheIdAndActiveTrue() {
        Long id = 300L;
        String name = "Gabriel";
        PersonEntity personEntity = repository.findByIdAndActiveTrue(id).get();

        Assert.assertNotNull(personEntity);
        Assert.assertEquals(name, personEntity.getName());
    }

    @Test(expected = PersonNotFoundException.class)
    public void mustNotFindPersonWithActiveFalse() {
        Long id = 301L;
        repository.findByIdAndActiveTrue(id).orElseThrow(PersonNotFoundException::new);
    }

    @Test
    public void mustReturnTrueWhenDocumentExists() {
        String document = "56309508008";
        Assert.assertTrue(repository.existsByDocument(document));
    }

    @Test
    public void mustReturnFalseWhenDocumentDoesNotExist() {
        String document = "11122234597";
        Assert.assertFalse(repository.existsByDocument(document));
    }

}
