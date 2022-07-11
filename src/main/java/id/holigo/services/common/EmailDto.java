package id.holigo.services.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private String to;

    private String from;

    private String subject;

    private String content;
}
