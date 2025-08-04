package guru.country.service;

import guru.country.domain.Country;
import guru.country.domain.graphql.CountryGql;
import guru.country.domain.graphql.CountryInputGql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {
    List<Country> allCountries();
    Page<Country> allCountries(Pageable pageable);
    List<CountryGql> allCountriesGpl();
    Country countryByCode(String code);
    CountryGql countryGqlByCode(String code);
    Country addCountry(Country country);
    CountryGql addCountryGql(CountryInputGql countryGql);
    void updateCountryNameByCode(Country country);
    void updateCountryGqlNameByCode(CountryInputGql country);
}
