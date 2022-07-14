package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

    static final long serialVersionUID = -65181210L;
    private Long id;

    private Long officialId;

    private UserParentDto parent;

    private String name;

    private String phoneNumber;

    private String email;

    private String verificationCode;

    private EmailStatusEnum emailStatus;

    private AccountStatusEnum accountStatus;

    private String mobileToken;

    private String type;

    private Long registerId;

    private String referral;

    private UserGroupEnum userGroup;

    List<UserDeviceDto> userDevices;

}
