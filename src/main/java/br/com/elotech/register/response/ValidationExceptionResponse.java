package br.com.elotech.register.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ValidationExceptionResponse {

    private String message;

    private List<String> errors;

    private LocalDateTime date;
}
