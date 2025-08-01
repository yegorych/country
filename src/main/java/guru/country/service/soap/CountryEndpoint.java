package guru.country.service.soap;

import guru.xml.country.*;
import org.springframework.data.domain.Page;
import guru.country.config.AppConfig;
import guru.country.service.CountryService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Endpoint
public class CountryEndpoint {
    private final CountryService countryService;

    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "codeRequest")
    @ResponsePayload
    public CountryResponse country(@RequestPayload CodeRequest request) {
        guru.country.domain.Country country = countryService.countryByCode(request.getCode());
        CountryResponse countryResponse = new CountryResponse();
        Country xmlCountry = new Country();
        xmlCountry.setCode(country.code());
        xmlCountry.setName(country.name());
        xmlCountry.setLastModifyDate(getXmlGregorianCalendar(country.lastModifyDate()));
        countryResponse.setCountry(xmlCountry);
        return countryResponse;
    }



    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "pageRequest")
    @ResponsePayload
    public CountriesResponse country(@RequestPayload PageRequest request) {
        Page<guru.country.domain.Country> countryPage = countryService.allCountries(
                org.springframework.data.domain.PageRequest.of(
                        request.getPage(),
                        request.getSize()
                )
        );
        CountriesResponse response = new CountriesResponse();
        response.setTotalPages(countryPage.getTotalPages());
        response.setTotalElements(countryPage.getTotalElements());
        response.getCountries().addAll(
                countryPage.getContent().stream().map(
                        country -> {
                            Country xmlCountry = new Country();
                            xmlCountry.setCode(country.code());
                            xmlCountry.setName(country.name());
                            xmlCountry.setLastModifyDate(getXmlGregorianCalendar(country.lastModifyDate()));
                            return xmlCountry;
                        }
                ).toList()
        );
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "countryInputRequest")
    @ResponsePayload
    public CountryResponse country(@RequestPayload CountryInputRequest request) {
        guru.country.domain.Country country = countryService.addCountry(
                new guru.country.domain.Country(
                        request.getName(),
                        request.getCode(),
                        new Date()
                )
        );

        CountryResponse countryResponse = new CountryResponse();
        Country xmlCountry = new Country();
        xmlCountry.setCode(country.code());
        xmlCountry.setName(country.name());
        xmlCountry.setLastModifyDate(getXmlGregorianCalendar(country.lastModifyDate()));
        countryResponse.setCountry(xmlCountry);
        return countryResponse;
    }


    private static XMLGregorianCalendar getXmlGregorianCalendar(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
