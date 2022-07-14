package id.holigo.services.common.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserEmailStatusDto implements Serializable {

    private Long userId;

    private EmailStatusEnum emailStatus;

}
