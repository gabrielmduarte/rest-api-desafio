package br.com.elotech.register.controller;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.exception.PersonNotFoundException;
import br.com.elotech.register.mapper.JsonMapper;
import br.com.elotech.register.repository.PersonRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void mustFindAllPersonsWithActiveTrue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", Matchers.is(4)))
                .andExpect(jsonPath("$.content[0].id", Matchers.is(304)))
                .andExpect(jsonPath("$.content[0].name", Matchers.is("Guilherme Segundo")))
                .andExpect(jsonPath("$.content[0].document", Matchers.is("73271931038")))
                .andExpect(jsonPath("$.content[0].dateOfBirth", Matchers.is("1959-05-16")))
                .andExpect(jsonPath("$.content[0].contacts.size()", Matchers.is(1)));
    }

    @Test
    public void mustFindAllPersonsWithActiveTrueFilteredByFullName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person?name={name}", "Guina")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", Matchers.is(1)))
                .andExpect(jsonPath("$.content[0].id", Matchers.is(302)))
                .andExpect(jsonPath("$.content[0].name", Matchers.is("Guina")))
                .andExpect(jsonPath("$.content[0].document", Matchers.is("08843306057")))
                .andExpect(jsonPath("$.content[0].dateOfBirth", Matchers.is("1980-10-16")))
                .andExpect(jsonPath("$.content[0].contacts.size()", Matchers.is(3)));
    }

    @Test
    public void mustFindAllPersonsWithActiveTrueFilteredByStartedName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person?name={name}", "Gui")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", Matchers.is(3)))
                .andExpect(jsonPath("$.content[0].id", Matchers.is(304)))
                .andExpect(jsonPath("$.content[0].name", Matchers.is("Guilherme Segundo")))
                .andExpect(jsonPath("$.content[0].document", Matchers.is("73271931038")))
                .andExpect(jsonPath("$.content[0].dateOfBirth", Matchers.is("1959-05-16")))
                .andExpect(jsonPath("$.content[0].contacts.size()", Matchers.is(1)));
    }

    @Test
    public void mustFindAllPersonsWithActiveTrueAndFilteredByDocument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person?document={document}", "563095")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", Matchers.is(300)))
                .andExpect(jsonPath("$.content[0].name", Matchers.is("Gabriel")))
                .andExpect(jsonPath("$.content[0].document", Matchers.is("56309508008")))
                .andExpect(jsonPath("$.content[0].dateOfBirth", Matchers.is("1997-07-05")))
                .andExpect(jsonPath("$.content[0].contacts[0].name", Matchers.is("guina")))
                .andExpect(jsonPath("$.content[0].contacts[0].phone", Matchers.is("16997980507")))
                .andExpect(jsonPath("$.content[0].contacts[0].email", Matchers.is("gabri@live.com")));
    }

    @Test
    public void mustFindAllPersonsWithActiveTrueAndFilteredByNameAndDocument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person?name={name}&document={document}", "Gabriel","563095")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", Matchers.is(1)))
                .andExpect(jsonPath("$.content[0].id", Matchers.is(300)))
                .andExpect(jsonPath("$.content[0].name", Matchers.is("Gabriel")))
                .andExpect(jsonPath("$.content[0].document", Matchers.is("56309508008")))
                .andExpect(jsonPath("$.content[0].dateOfBirth", Matchers.is("1997-07-05")))
                .andExpect(jsonPath("$.content[0].contacts[0].name", Matchers.is("guina")))
                .andExpect(jsonPath("$.content[0].contacts[0].phone", Matchers.is("16997980507")))
                .andExpect(jsonPath("$.content[0].contacts[0].email", Matchers.is("gabri@live.com")));
    }

    @Test
    public void mustFindOnePersonsWithActiveTrue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", 300L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Gabriel")))
                .andExpect(jsonPath("$.document", Matchers.is("56309508008")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("1997-07-05")))
                .andExpect(jsonPath("$.contacts[0].name", Matchers.is("guina")))
                .andExpect(jsonPath("$.contacts[0].phone", Matchers.is("16997980507")))
                .andExpect(jsonPath("$.contacts[0].email", Matchers.is("gabri@live.com")));
    }

    @Test
    public void mustNotFindOnePersonsWithActiveFalse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", 301L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void mustReturnBadRequestWhenReceivingUnregisteredId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", 15L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void mustReturn201WhenCreateAPerson() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","088.795.140-67",
                LocalDate.of(2000, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));

        PersonEntity personEntity = personRepository.findByIdAndActiveTrue(1L)
                .orElseThrow(PersonNotFoundException::new);

        Assert.assertEquals("Lewis Hamilton", personEntity.getName());
        Assert.assertEquals(true, personEntity.isActive());
        Assert.assertEquals("08879514067", personEntity.getDocument());
        Assert.assertEquals(LocalDate.of(2000, 10, 10), personEntity.getDateOfBirth());
    }

    @Test
    public void mustReturn201WhenCreateAPersonWithMoreThanOneContact() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Agnaldo Duarte","847.136.490-58",
                LocalDate.of(2000, 10, 10),2);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));

        PersonEntity personEntity = personRepository.findByIdAndActiveTrue(1L)
                .orElseThrow(PersonNotFoundException::new);


        Assert.assertEquals("Agnaldo Duarte", personEntity.getName());
        Assert.assertEquals(true, personEntity.isActive());
        Assert.assertEquals("84713649058", personEntity.getDocument());
        Assert.assertEquals(LocalDate.of(2000, 10, 10), personEntity.getDateOfBirth());
    }

    @Test
    public void mustReturn400WhenReceivingInvalidDocument() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","111.222.333-00",
                LocalDate.of(2000, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenReceivingEmailWithoutSintax() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPersonAndContact("Lewis Hamilton","088.795.140-67",
                                                    LocalDate.of(2000, 10, 10),
                                        "Gabriel","16778899567","xxxyyyynghs");
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn409WhenReceivingDocumentAlreadyRegisteredWithoutPunctuation() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","56309508008",
                LocalDate.of(2000, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }

    @Test
    public void mustReturn409WhenReceivingDocumentAlreadyRegisteredWithPunctuation() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","563.095.080-08",
                LocalDate.of(2000, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }

    @Test
    public void mustReturn400WhenNotReceivingAttributes() throws Exception {
        URI uri = new URI("/person");
        String json = "";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenReceiveFutureDateOfBirth() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","563.095.080-08",
                LocalDate.of(2030, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingNameAttribute() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("","446.597.450-74",
                LocalDate.of(2030, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingDocumentAttribute() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPerson("Lewis Hamilton","",
                LocalDate.of(2000, 10, 10),1);
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingDateOfBirthAttribute() throws Exception {
        URI uri = new URI("/person");
        String json = "{\"name\":\"Agnaldo Duarte\",\"documentRequest\":" +
                "\"088.795.140-67\",\"contacts\":[{\"name\":\"Agnaldo\"," +
                "\"phone\":\"16997980507\",\"email\":\"gabri.com@live.com\"}]}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingContacts() throws Exception {
        URI uri = new URI("/person");
        String json = "{\"name\":\"Agnaldo Duarte\",\"documentRequest\":" +
                "\"088.795.140-67\",\"dateOfBirth\":\"2019-07-05\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingContactName() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPersonAndContact("Lewis Hamilton","088.795.140-67",
                                                        LocalDate.of(2000, 10, 10),
                                            "","16778899567","gabri@gmail.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingContactPhone() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPersonAndContact("Lewis Hamilton","088.795.140-67",
                LocalDate.of(2000, 10, 10),
                "Unit Test","","gabri@gmail.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenNotReceivingContactEmail() throws Exception {
        URI uri = new URI("/person");

        PersonEntity person = createPersonAndContact("Lewis Hamilton","088.795.140-67",
                LocalDate.of(2000, 10, 10),
                "Unit Test","16778899567","");
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn200WhenUpdatePerson() throws Exception {
        PersonEntity person = createPersonUpdate("Update Test", LocalDate.of(1990, 05, 07));
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{id}", 302L).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        PersonEntity personEntity = personRepository.findByIdAndActiveTrue(302L).get();

        Assert.assertEquals("Update Test", personEntity.getName());
        Assert.assertEquals(LocalDate.of(1990, 05, 07), personEntity.getDateOfBirth());
        Assert.assertEquals(true, personEntity.isActive());
    }

    @Test
    public void mustReturn400WhenRequestHasNameEmpty() throws Exception {
        PersonEntity person = createPersonUpdate("", LocalDate.of(1990, 05, 07));
        String json = jsonMapper.getObjectMapper().writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{id}", 302L).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn400WhenRequestHasDateEmpty() throws Exception {
        String json = "{\"name\":\"Agnaldo Duarte\",\"documentRequest\":" +
                "\"088.795.140-67\",\"dateOfBirth\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{id}", 302L).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void mustReturn200WhenDeletePerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{id}", 302L))
                .andExpect(status().is(200));

        PersonEntity excluded = personRepository.findById(302L).get();

        Assert.assertEquals(false, excluded.isActive());
        Assert.assertEquals("0884330605", excluded.getDocument());
    }

    @Test
    public void mustReturn404WhenDeletePersonReceiveIdNotRegistered() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{id}", 50L))
                .andExpect(status().is(404));
    }

    private PersonEntity createPerson(String name, String document, LocalDate birth, int totalContacts) {
        ArrayList<ContactEntity> contacts = new ArrayList<>();

        for (int i = 0; i < totalContacts; i++) {
            ContactEntity contactEntity = new ContactEntity();
            contactEntity.setEmail("method@gmail.com");
            contactEntity.setName("Method contact " + (i+1));
            contactEntity.setPhone("1199999000"+(i+1));

            contacts.add(contactEntity);
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(name);
        personEntity.setDocument(document);
        personEntity.setDateOfBirth(birth);
        personEntity.setContacts(contacts);

        return personEntity;
    }

    private PersonEntity createPersonAndContact(String name, String document, LocalDate birth,
                                                String contactName, String contactPhone, String contactEmail) {
        ArrayList<ContactEntity> contacts = new ArrayList<>();

        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setEmail(contactEmail);
        contactEntity.setName(contactName);
        contactEntity.setPhone(contactPhone);
        contacts.add(contactEntity);

        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(name);
        personEntity.setDocument(document);
        personEntity.setDateOfBirth(birth);
        personEntity.setContacts(contacts);

        return personEntity;
    }

    private PersonEntity createPersonUpdate(String name, LocalDate birth) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(name);
        personEntity.setDateOfBirth(birth);

        return personEntity;
    }

}
