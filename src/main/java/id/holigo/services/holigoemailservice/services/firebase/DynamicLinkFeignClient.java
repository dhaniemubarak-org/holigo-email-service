package id.holigo.services.holigoemailservice.services.firebase;

import id.holigo.services.holigoemailservice.web.model.DynamicLinkDto;
import id.holigo.services.holigoemailservice.web.model.RequestDynamicLinkDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "firebase-service", url = "https://firebasedynamiclinks.googleapis.com")
public interface DynamicLinkFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/shortLinks")
    ResponseEntity<DynamicLinkDto> postGetLink(@RequestParam("key") String key, @RequestBody RequestDynamicLinkDto requestDynamicLinkDto);
}
