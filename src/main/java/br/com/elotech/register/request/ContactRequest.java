package br.com.elotech.register.request;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ContactRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

}
