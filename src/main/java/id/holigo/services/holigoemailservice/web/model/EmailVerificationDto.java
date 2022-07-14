package id.holigo.services.holigoemailservice.web.model;

import id.holigo.services.common.model.EmailStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDto implements Serializable {

    private UUID id;

    private String email;

    private Timestamp expiredAt;

    private EmailStatusEnum status;

    private String note;

    private Timestamp createdAt;

    private final Timestamp serverTime = Timestamp.valueOf(LocalDateTime.now());
}
