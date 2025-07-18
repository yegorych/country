package guru.country.controller.graphql;


import guru.country.domain.graphql.CountryGql;
import guru.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class CountryQueryController {

    private final CountryService countryService;

    @Autowired
    public CountryQueryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @QueryMapping
    public List<CountryGql> countries() {
        return countryService.allCountriesGpl();
    }

    @QueryMapping
    public CountryGql country(@Argument String code) {
        return countryService.countryGqlByCode(code);
    }


}
