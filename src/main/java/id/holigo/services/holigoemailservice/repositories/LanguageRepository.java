package id.holigo.services.holigoemailservice.repositories;

import id.holigo.services.holigoemailservice.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

    Language findByMessageKeyAndLocale(String messageKey, String locale);
}
