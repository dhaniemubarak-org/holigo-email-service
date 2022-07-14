package id.holigo.services.holigoemailservice.services.user;

import id.holigo.services.common.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public UserDto getUser(Long userId) {
        ResponseEntity<UserDto> responseEntity = userServiceFeignClient.getUser(userId);
        return responseEntity.getBody();
    }
}
