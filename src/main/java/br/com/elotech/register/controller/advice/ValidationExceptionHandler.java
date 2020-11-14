package br.com.elotech.register.controller.advice;

import br.com.elotech.register.exception.ContactNotFoundInPersonListException;
import br.com.elotech.register.exception.DocumentAlreadyExistException;
import br.com.elotech.register.exception.ListOfContactCantBeEmptyException;
import br.com.elotech.register.response.ContactNotFoundInPersonListResponse;
import br.com.elotech.register.response.DocumentAlreadyExistResponse;
import br.com.elotech.register.response.ValidationExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationExceptionResponse handleArgumentNotValid(MethodArgumentNotValidException e) {
        log.info("M=handleArgumentNotValid, I=Invalid parameters, Message={}", e.getMessage());
        List<String> errors = e.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(ObjectError::getDefaultMessage)
                                .collect(Collectors.toList());

        ValidationExceptionResponse response = new ValidationExceptionResponse();

        response.setMessage("Field validation error. Please check your request");
        response.setErrors(errors);
        response.setDate(LocalDateTime.now());

        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DocumentAlreadyExistException.class)
    public DocumentAlreadyExistResponse handleUniqueKeyException(DocumentAlreadyExistException e) {
        log.info("M=handleUniqueKeyException, I=Data integrity violation, " +
                "message=document already exist");

        DocumentAlreadyExistResponse response = new DocumentAlreadyExistResponse();

        response.setMessage("Document already exists. Please check your request");
        response.setDate(LocalDateTime.now());

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactNotFoundInPersonListException.class)
    public ContactNotFoundInPersonListResponse handleContactNotFoundInPersonListException(ContactNotFoundInPersonListException e) {
        log.info("M=handleContactNotFoundInPersonListException, I=Contact not found in person list");

        ContactNotFoundInPersonListResponse response = new ContactNotFoundInPersonListResponse();

        response.setMessage("Contact not found in person list");
        response.setDate(LocalDateTime.now());

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ListOfContactCantBeEmptyException.class)
    public ListOfContactCantBeEmptyException handleListOfContactCantBeEmptyException(ListOfContactCantBeEmptyException e) {
        log.info("M=handleListOfContactCantBeEmptyException, I= Person contact list can't be empty");
        return new ListOfContactCantBeEmptyException();
    }

}
