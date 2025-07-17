package guru.country.service;

import guru.country.domain.Country;
import guru.country.domain.graphql.CountryGql;
import guru.country.domain.graphql.CountryInputGql;

import java.util.List;

public interface CountryService {
    List<Country> allCountries();
    List<CountryGql> allCountriesGpl();
    Country countryByCode(String code);
    CountryGql countryGqlByCode(String code);
    Country addCountry(Country country);
    CountryGql addCountryGql(CountryInputGql countryGql);
    void updateCountryNameByCode(Country country);
    void updateCountryGqlNameByCode(CountryInputGql country);
}
