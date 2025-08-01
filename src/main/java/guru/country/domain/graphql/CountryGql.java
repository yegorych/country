package guru.country.domain.graphql;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

public record CountryGql(@NonNull UUID id,
                         @NonNull String name,
                         @Nullable String code,
                         @Nullable Date lastModifyDate) {
}

