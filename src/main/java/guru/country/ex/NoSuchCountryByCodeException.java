package guru.country.ex;

public class NoSuchCountryByCodeException extends RuntimeException {
    public NoSuchCountryByCodeException(String message) {
        super(message);
    }
}
