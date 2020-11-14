package br.com.elotech.register.mapper;

import br.com.elotech.register.domain.PersonEntity;
import br.com.elotech.register.request.PersonRequest;
import br.com.elotech.register.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ContactMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    PersonEntity toEntity(final PersonRequest personRequest);

    PersonResponse toResponse(final PersonEntity personEntity);
}
