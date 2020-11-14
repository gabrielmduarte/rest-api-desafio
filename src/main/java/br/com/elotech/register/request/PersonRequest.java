package br.com.elotech.register.request;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Data
public class PersonRequest {

    @NotBlank(message = "Name field's required")
    private String name;

    @CPF(message = "Document invalid.")
    @NotBlank(message = "Document field's required")
    private String document;

    @NotNull(message = "Date of birth field's required")
    @PastOrPresent(message = "Date of birth field cannot be a future date")
    private LocalDate dateOfBirth;

    @Valid
    @NotNull(message = "It's mandatory to register at least one contact")
    private List<ContactRequest> contacts;

}
