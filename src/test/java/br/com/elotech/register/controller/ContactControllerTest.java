package br.com.elotech.register.controller;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.exception.ContactNotFoundException;
import br.com.elotech.register.exception.PersonNotFoundException;
import br.com.elotech.register.mapper.JsonMapper;
import br.com.elotech.register.repository.ContactRepository;
import br.com.elotech.register.repository.PersonRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void mustReturn201WhenCreateNewContact() throws Exception {
        ContactEntity contact = createContact("Teste ContactController", "1100223344", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/person/{id}/contact", 302)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

        ContactEntity contactEntity = getPersonEntity(302L).getContacts().get(3);
        Assert.assertEquals(contact.getName(), contactEntity.getName());
        Assert.assertEquals(contact.getEmail(), contactEntity.getEmail());
        Assert.assertEquals(contact.getPhone(), contactEntity.getPhone());
    }

    @Test
    public void mustReturn400WhenSendNewContactWithoutName() throws Exception {
        ContactEntity contact = createContactWithoutName("1100223344", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/person/{id}/contact", 302)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenValidWrongSyntaxEmail() throws Exception {
        ContactEntity contact = createContact("1100223344", "16554467654", "ksgajjhdne2");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/person/{id}/contact", 302)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenSendNewContactWithoutEmail() throws Exception {
        ContactEntity contact = createContactWithoutEmail("gabri duarte", "167784765554");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/person/{id}/contact", 302)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenSendNewContactWithoutPhone() throws Exception {
        ContactEntity contact = createContactWithoutPhone("gabri duarte", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/person/{id}/contact", 302)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn200WhenUpdateContact() throws Exception {
        ContactEntity contactEntity = getPersonEntity(302L).getContacts().get(0);
        Assert.assertEquals("biel", contactEntity.getName());
        Assert.assertEquals("gabri@live.com", contactEntity.getEmail());
        Assert.assertEquals("16997980507", contactEntity.getPhone());

        ContactEntity contact = createContact("Teste ContactController", "1100223344", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 302, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        ContactEntity contactEntityUpdated = getPersonEntity(302L).getContacts().get(0);
        Assert.assertEquals(contact.getName(), contactEntityUpdated.getName());
        Assert.assertEquals(contact.getEmail(), contactEntityUpdated.getEmail());
        Assert.assertEquals(contact.getPhone(), contactEntityUpdated.getPhone());
    }

    @Test
    public void mustReturn400WhenTryToUpdateContactThatIsNotInPersonList() throws Exception {
        ContactEntity contact = createContact("Gabriel", "1100223344", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 304, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenTryToUpdateContactWithoutName() throws Exception {
        ContactEntity contact = createContactWithoutName("1100223344", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 302, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400TryToUpdateWithWrongSyntaxEmail() throws Exception {
        ContactEntity contact = createContact("1100223344", "16554467654", "ksgajjhdne2");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 302, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenTryToUpdateContactWithoutEmail() throws Exception {
        ContactEntity contact = createContactWithoutEmail("gabri duarte", "167784765554");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 302, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenTryToUpdateContactWithoutPhone() throws Exception {
        ContactEntity contact = createContactWithoutPhone("gabri duarte", "contact@live.com");
        String json = jsonMapper.getObjectMapper().writeValueAsString(contact);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/person/{personId}/contact/{contactId}", 302, 303)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn200WhenDeleteContact() throws Exception {
        int sizeBeforeDelete = getPersonEntity(302L).getContacts().size();
        Assert.assertEquals(3, sizeBeforeDelete);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{personId}/contact/{contactId}", 302, 303))
                .andExpect(MockMvcResultMatchers.status().is(200));

        int sizeAfterDelete = getPersonEntity(302L).getContacts().size();
        Assert.assertEquals(2, sizeAfterDelete);
    }

    @Test
    public void mustReturn400WhenTryToDeleteOnlyContactInPersonList() throws Exception {
        PersonEntity personEntity = personRepository.findByIdAndActiveTrue(300L)
                                                        .orElseThrow(PersonNotFoundException::new);
        Assert.assertEquals(1, personEntity.getContacts().size());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{personId}/contact/{contactId}", 300, 300))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void mustReturn400WhenTryToDeleteContactThatIsNotInPersonList() throws Exception {
        PersonEntity personEntity = personRepository.findByIdAndActiveTrue(302L)
                                                     .orElseThrow(PersonNotFoundException::new);
        ContactEntity contactEntity = contactRepository.findById(300L)
                                                        .orElseThrow(ContactNotFoundException::new);

        Assert.assertFalse(personEntity.getContacts().contains(contactEntity));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{personId}/contact/{contactId}", 300, 300))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    private PersonEntity getPersonEntity(final Long id) {
        return personRepository.findByIdAndActiveTrue(id).orElseThrow(PersonNotFoundException::new);
    }

    private ContactEntity createContact(String name, String phone, String email) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setPhone(phone);
        contactEntity.setName(name);
        contactEntity.setEmail(email);

        return contactEntity;
    }

    private ContactEntity createContactWithoutEmail(String name, String phone) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setPhone(phone);
        contactEntity.setName(name);

        return contactEntity;
    }

    private ContactEntity createContactWithoutPhone(String name, String email) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setName(name);
        contactEntity.setEmail(email);

        return contactEntity;
    }

    private ContactEntity createContactWithoutName(String phone, String email) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setPhone(phone);
        contactEntity.setEmail(email);

        return contactEntity;
    }

}
