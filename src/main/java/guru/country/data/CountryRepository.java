package guru.country.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
    Optional<CountryEntity> findCountryEntityByCode(String code);

}
