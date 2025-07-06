package guru.country.service;

import guru.country.data.CountryEntity;
import guru.country.data.CountryRepository;
import guru.country.domain.Country;
import guru.country.ex.NoSuchCountryByCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DbCountryService implements CountryService{
    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> allCountries() {
        return countryRepository.findAll()
                .stream()
                .map(countryEntity -> new Country(
                        countryEntity.getName(),
                        countryEntity.getCode(),
                        countryEntity.getLastModifyDate()
                ))
                .toList();

    }

    @Override
    public void addCountry(Country country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());
        countryEntity.setLastModifyDate(new Date());
        countryRepository.save(countryEntity);
    }

    @Override
    public void updateCountryNameByCode(Country country) {
        countryRepository.findCountryEntityByCode(country.code()).ifPresentOrElse(
                countryEntity -> {
                    countryEntity.setName(country.name());
                    countryEntity.setLastModifyDate(new Date());
                    countryEntity.setCode(country.code());
                    countryRepository.save(countryEntity);
                },
                ()-> {throw new NoSuchCountryByCodeException("No such country");}
        );
    }


}
