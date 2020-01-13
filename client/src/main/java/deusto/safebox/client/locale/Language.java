package deusto.safebox.client.locale;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public enum Language {

    ENGLISH("English", "en"),
    SPANISH("Español", "es"),
    ;

    private static final Map<String, Language> CODE_MAPPER
            = Arrays.stream(values()).collect(Collectors.toMap(Language::getCode, e -> e));

    private final String name;
    private final String code;

    Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Get a {@link Language} from its code.
     *
     * @param code the language code.
     * @return the language with the specified code,
     *          or an empty {@link Optional} if the code doesn't match any.
     */
    public static Optional<Language> fromCode(String code) {
        return Optional.ofNullable(CODE_MAPPER.get(code));
    }
}
