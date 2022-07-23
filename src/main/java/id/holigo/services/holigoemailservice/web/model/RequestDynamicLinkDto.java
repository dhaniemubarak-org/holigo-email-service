package id.holigo.services.holigoemailservice.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RequestDynamicLinkDto implements Serializable {
    private String longDynamicLink;
}
