package br.com.elotech.register.request;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class UpdatePersonRequest {

    @NotBlank(message = "Name field's required")
    private String name;

    @NotNull(message = "Date of birth field's required")
    @PastOrPresent(message = "Date of birth field cannot be a future date")
    private LocalDate dateOfBirth;

}
