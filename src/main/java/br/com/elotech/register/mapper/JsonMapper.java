package br.com.elotech.register.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

    private ObjectMapper objectMapper;

    public JsonMapper() {
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .modules(new JSR310Module()).build();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
