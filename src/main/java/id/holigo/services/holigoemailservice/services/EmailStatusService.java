package id.holigo.services.holigoemailservice.services;

import java.util.UUID;

public interface EmailStatusService {

    void successConfirmation(UUID id);

    void expiryConfirmation(UUID id);
}
