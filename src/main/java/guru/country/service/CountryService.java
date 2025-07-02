package guru.country.service;

import guru.country.domain.Country;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();
    void addCountry(Country country);
    void updateCountryNameByCode(Country country);


}
