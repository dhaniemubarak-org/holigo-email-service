package id.holigo.services.holigoemailservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto implements Serializable {

    private String to;

    private String from;

    private String subject;

    private String content;
}
