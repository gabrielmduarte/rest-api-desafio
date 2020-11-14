package br.com.elotech.register.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactNotFoundInPersonListResponse {

    private String message;

    private LocalDateTime date;

}
