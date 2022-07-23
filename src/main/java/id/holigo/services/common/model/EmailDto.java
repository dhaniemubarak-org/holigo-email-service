package id.holigo.services.common.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto implements Serializable {

    private String to;

    private String from;

    private String subject;

    private String template;

    private Map<String, Object> data;
}
