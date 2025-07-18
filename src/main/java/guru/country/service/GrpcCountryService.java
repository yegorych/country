package guru.country.service;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import guru.country.domain.Country;
import guru.grpc.country.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {
    private final static Logger log = LoggerFactory.getLogger(GrpcCountryService.class);

    private final CountryService countryService;

    public GrpcCountryService(CountryService countryService) {
        this.countryService =  countryService;
    }

    @Override
    public void country(CodeRequest request, StreamObserver<CountryResponse> responseObserver) {
        final Country country = countryService.countryByCode(request.getCode());
        assert country.lastModifyDate() != null;
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setCode(country.code())
                        .setName(country.name())
                        .setLastModifyDate(Timestamps.fromDate(country.lastModifyDate()))
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CountryRequest> addCountries(StreamObserver<Count> responseObserver) {
        return new StreamObserver<>() {
            int count = 0;
            @Override
            public void onNext(CountryRequest request) {
                countryService.addCountry(
                        new Country(
                                request.getName(),
                                request.getCode(),
                                new Date()
                        )
                );
                count++;
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Count.newBuilder().setCount(count).build());
                responseObserver.onCompleted();
            }
        };
    }


    @Override
    public void allCountry(Empty request, StreamObserver<CountryResponse> responseObserver) {
        List<Country> countries = countryService.allCountries();
        for (Country country : countries) {
            assert country.lastModifyDate() != null;
            responseObserver.onNext(
                    CountryResponse.newBuilder()
                            .setCode(country.code())
                            .setName(country.name())
                            .setLastModifyDate(Timestamps.fromDate(country.lastModifyDate()))
                            .build()
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        Country country = countryService.addCountry(
                new Country(
                        request.getName(),
                        request.getCode(),
                        new Date()
                )
        );
        assert country.lastModifyDate() != null;
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setCode(country.code())
                        .setName(country.name())
                        .setLastModifyDate(Timestamps.fromDate(country.lastModifyDate()))
                        .build()
        );
        responseObserver.onCompleted();

    }
}
