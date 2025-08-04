package guru.country.domain.graphql;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record CountryInputGql(@NonNull String name,
                              @Nullable String code) {
}
