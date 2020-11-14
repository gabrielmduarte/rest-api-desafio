package br.com.elotech.register.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonResponse {

    private Long id;

    private String name;

    private String document;

    private LocalDate dateOfBirth;

    private List<ContactResponse> contacts;

    private boolean active;
}
