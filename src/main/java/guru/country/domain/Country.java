package guru.country.domain;

import guru.country.data.CountryEntity;
import guru.country.dto.CountryDto;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Date;

public record Country(@NonNull String name,
                      @Nullable String code,
                      @Nullable Date lastModifyDate) {

    public static Country fromCountryDto(CountryDto countryDto){
        return new Country(
                countryDto.name(),
                countryDto.code(),
                countryDto.lastModifyDate() == null ? new  Date() : countryDto.lastModifyDate()
        );
    }

    public  static Country fromCountryEntity(CountryEntity country){
        return new Country(country.getName(), country.getCode(), country.getLastModifyDate());
    }
}
