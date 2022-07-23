package id.holigo.services.holigoemailservice.services.firebase;

import id.holigo.services.holigoemailservice.web.model.DynamicLinkDto;
import id.holigo.services.holigoemailservice.web.model.RequestDynamicLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DynamicLinkServiceFeign implements DynamicLinkService {

    @Value("${firebase.dynamicLink.key}")
    private String key;

    private DynamicLinkFeignClient dynamicLinkFeignClient;

    @Autowired
    public void setDynamicLinkFeignClient(DynamicLinkFeignClient dynamicLinkFeignClient) {
        this.dynamicLinkFeignClient = dynamicLinkFeignClient;
    }

    @Override
    public DynamicLinkDto getShortLink(String link) {
        ResponseEntity<DynamicLinkDto> response = dynamicLinkFeignClient.postGetLink(key, RequestDynamicLinkDto.builder()
                .longDynamicLink(link).build());
        return response.getBody();
    }
}
