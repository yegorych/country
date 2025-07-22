package guru.country.service;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import guru.country.data.CountryEntity;
import guru.country.data.CountryRepository;
import guru.country.domain.Country;
import guru.country.ex.NoSuchCountryByCodeException;
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

    private final CountryRepository countryRepository;

    public GrpcCountryService(CountryRepository countryRepository) {
        this.countryRepository =  countryRepository;
    }

    @Override
    public void country(CodeRequest request, StreamObserver<CountryResponse> responseObserver) {
        final Country country = countryRepository.findCountryEntityByCode(request.getCode())
                .map(Country::instance)
                .orElseThrow(() -> new NoSuchCountryByCodeException("No such country"));

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
                CountryEntity countryEntity = new CountryEntity();
                countryEntity.setName(request.getName());
                countryEntity.setCode(request.getCode());
                countryEntity.setLastModifyDate(new Date());
                countryRepository.save(countryEntity);
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
        List<Country> countries =  countryRepository.findAll()
                .stream()
                .map(countryEntity -> new Country(
                        countryEntity.getName(),
                        countryEntity.getCode(),
                        countryEntity.getLastModifyDate()
                ))
                .toList();

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
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(request.getName());
        countryEntity.setCode(request.getCode());
        countryEntity.setLastModifyDate(new Date());
        Country country = Country.instance(countryRepository.save(countryEntity));

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
