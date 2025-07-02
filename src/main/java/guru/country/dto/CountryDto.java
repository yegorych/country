package guru.country.dto;

import guru.country.domain.Country;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Date;

public record CountryDto(@NonNull String name,
                         @Nullable String code,
                         @Nullable Date lastModifyDate) {

    public static CountryDto fromCountry(Country country){
        return new CountryDto(
                country.name(),
                country.code(),
                country.lastModifyDate() == null ? new Date() : country.lastModifyDate()
        );
    }
}
