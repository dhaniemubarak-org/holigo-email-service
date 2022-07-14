package id.holigo.services.holigoemailservice.services.user;

import id.holigo.services.common.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "holigo-user-service", url = "localhost:9999")
public interface UserServiceFeignClient {

    String USER_DETAIL = "/api/v1/completeUsers/{id}";

    @RequestMapping(method = RequestMethod.GET, value = USER_DETAIL)
    ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId);
}
