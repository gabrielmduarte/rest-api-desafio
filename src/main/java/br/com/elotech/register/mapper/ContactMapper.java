package br.com.elotech.register.mapper;

import br.com.elotech.register.domain.ContactEntity;
import br.com.elotech.register.request.ContactRequest;
import br.com.elotech.register.response.ContactResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactMapper {

    ContactEntity toEntity(final ContactRequest contactRequest);

    List<ContactEntity> toListOfEntitys(final List<ContactRequest> contactRequest);

    ContactResponse toResponse(final ContactEntity contactEntity);

    List<ContactResponse> toListOfResponses(final List<ContactEntity> contactEntity);
}
