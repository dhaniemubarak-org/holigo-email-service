package id.holigo.services.holigoemailservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DynamicLinkWarningDto implements Serializable {
    private String warningCode;

    private String warningMessage;
}
