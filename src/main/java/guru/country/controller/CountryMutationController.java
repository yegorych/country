package guru.country.controller;


import guru.country.domain.graphql.CountryGql;
import guru.country.domain.graphql.CountryInputGql;
import guru.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


@Controller
public class CountryMutationController {

    private final CountryService countryService;

    @Autowired
    public CountryMutationController(CountryService countryService) {
        this.countryService = countryService;
    }

    @MutationMapping
    public CountryGql add(@Argument CountryInputGql input) {
        return countryService.addCountryGql(input);
    }

    @MutationMapping
    public void update(@Argument CountryInputGql input) {
        countryService.updateCountryGqlNameByCode(input);
    }
}
