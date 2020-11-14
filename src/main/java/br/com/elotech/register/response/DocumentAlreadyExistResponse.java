package br.com.elotech.register.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentAlreadyExistResponse {

    private String message;

    private LocalDateTime date;

}
