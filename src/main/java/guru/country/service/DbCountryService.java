package guru.country.service;

import guru.country.data.CountryEntity;
import guru.country.data.CountryRepository;
import guru.country.domain.Country;
import guru.country.domain.graphql.CountryGql;
import guru.country.domain.graphql.CountryInputGql;
import guru.country.ex.NoSuchCountryByCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DbCountryService implements CountryService {
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
    public List<CountryGql> allCountriesGpl() {
        return countryRepository.findAll()
                .stream()
                .map(countryEntity -> new CountryGql(
                        countryEntity.getId(),
                        countryEntity.getName(),
                        countryEntity.getCode(),
                        countryEntity.getLastModifyDate()
                ))
                .toList();
    }

    @Override
    public Country addCountry(Country country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());
        countryEntity.setLastModifyDate(new Date());
        return Country.instance(countryRepository.save(countryEntity));
    }

    @Override
    public CountryGql addCountryGql(CountryInputGql country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());
        countryEntity.setLastModifyDate(new Date());
        CountryEntity ce = countryRepository.save(countryEntity);
        return new CountryGql(ce.getId(),
                ce.getName(),
                ce.getCode(),
                ce.getLastModifyDate()
        );
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

    @Override
    public void updateCountryGqlNameByCode(CountryInputGql country) {
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


    @Override
    public Country countryByCode(String code) {
        return countryRepository.findCountryEntityByCode(code)
                .map(Country::instance)
                .orElseThrow(() -> new NoSuchCountryByCodeException("No such country"));
    }

    @Override
    public CountryGql countryGqlByCode(String code) {
        return countryRepository.findCountryEntityByCode(code)
                .map(ce ->
                        new CountryGql(ce.getId(),
                                ce.getName(),
                                ce.getCode(),
                                ce.getLastModifyDate())
                )
                .orElseThrow(() -> new NoSuchCountryByCodeException("No such country"));
    }


}
