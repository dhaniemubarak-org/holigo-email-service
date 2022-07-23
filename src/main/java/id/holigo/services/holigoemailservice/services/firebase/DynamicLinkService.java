package id.holigo.services.holigoemailservice.services.firebase;

import id.holigo.services.holigoemailservice.web.model.DynamicLinkDto;

public interface DynamicLinkService {

    DynamicLinkDto getShortLink(String link);
}
