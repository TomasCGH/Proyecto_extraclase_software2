package co.edu.uco.backendvictus.crosscutting.helpers;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * Helper centralizado para sanitizar entradas de usuarios utilizando OWASP HTML Sanitizer.
 */
public final class Sanitizer {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols()
            .toFactory();

    private Sanitizer() {
        // Utility class
    }

    public static String sanitize(final String input) {
        if (input == null) {
            return null;
        }
        final String cleaned = POLICY.sanitize(input);
        return cleaned.trim().replaceAll("\\s+", " ");
    }

    public static Map<String, Object> sanitizeMetadata(final Map<String, Object> metadatos) {
        if (metadatos == null) {
            return null;
        }
        return metadatos.entrySet().stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(entry -> sanitize(entry.getKey()), Sanitizer::sanitizeValue,
                                (existing, replacement) -> replacement, LinkedHashMap::new),
                        Collections::unmodifiableMap));
                .collect(Collectors.toUnmodifiableMap(entry -> sanitize(entry.getKey()), Sanitizer::sanitizeValue));
    }

    private static Object sanitizeValue(final Object value) {
        if (value instanceof String stringValue) {
            return sanitize(stringValue);
        }
        if (value instanceof Map<?, ?> nested) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> casted = nested.entrySet().stream()
                    .collect(Collectors.toMap(entry -> Objects.toString(entry.getKey()), Map.Entry::getValue,
                            (existing, replacement) -> replacement, LinkedHashMap::new));
                    .collect(Collectors.toMap(entry -> Objects.toString(entry.getKey()), entry -> entry.getValue()));
            return sanitizeMetadata(casted);
        }
        return value;
    }
}
