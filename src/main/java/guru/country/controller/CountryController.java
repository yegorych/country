package guru.country.controller;


import guru.country.domain.Country;
import guru.country.dto.CountryDto;
import guru.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<CountryDto> all(){
        return countryService.allCountries().stream().map(CountryDto::fromCountry).toList();
    }

    @PostMapping("/add")
    public void add(@RequestBody CountryDto country) {
        countryService.addCountry(Country.instance(country));
    }

    @PatchMapping("/update/{code}")
    public void update(@PathVariable String code, @RequestBody CountryDto country) {
        countryService.updateCountryNameByCode(new Country(country.name(), code, new Date()));
    }


}
