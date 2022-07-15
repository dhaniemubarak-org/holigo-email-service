package id.holigo.services.holigoemailservice.web.mappers;

import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(EmailVerificationMapperDecorator.class)
@Mapper
public interface EmailVerificationMapper {
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    EmailVerificationDto emailVerificationToEmailVerificationDto(EmailVerification emailVerification);

    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "indexNote", ignore = true)
    EmailVerification emailVerificationDtoToEmailVerification(EmailVerificationDto emailVerificationDto);
}
