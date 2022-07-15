package id.holigo.services.holigoemailservice.web.model;

import id.holigo.services.common.model.EmailStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDto implements Serializable {

    private UUID id;

    private String email;

    public String imageUrl;

    private Timestamp expiredAt;

    private EmailStatusEnum status;

    private List<String> notes;

    private Timestamp createdAt;

    private final Timestamp serverTime = Timestamp.valueOf(LocalDateTime.now());
}
