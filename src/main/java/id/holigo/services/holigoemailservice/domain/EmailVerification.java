package id.holigo.services.holigoemailservice.domain;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.common.model.EmailTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailVerification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private Long userId;

    private String email;

    private Timestamp expiredAt;

    @Enumerated(EnumType.STRING)
    private EmailTypeEnum type;

    @Enumerated(EnumType.STRING)
    private EmailStatusEnum status;

    private String indexNote;

    private String verificationCode;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;


}
