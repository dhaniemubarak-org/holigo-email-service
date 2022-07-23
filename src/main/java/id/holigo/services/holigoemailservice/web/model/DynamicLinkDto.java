package id.holigo.services.holigoemailservice.web.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties
public class DynamicLinkDto implements Serializable {

    private String shortLink;

    private List<DynamicLinkWarningDto> warning;

}
