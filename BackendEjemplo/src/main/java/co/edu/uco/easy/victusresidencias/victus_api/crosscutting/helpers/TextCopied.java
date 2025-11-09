package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for text and number parsing operations.
 * This class cannot be instantiated.
 */
public final class TextCopied {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextCopied.class);
    
    private TextCopied() {
        // Private constructor to prevent instantiation
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }
    /**
     * Parses a string to a Double. Returns null if the string is null or not a valid number.
     * 
     * @param number the string to parse
     * @return the parsed Double value, or null if the input is invalid
     */
    public static Double parseNumber(final String number) {
        // Validamos si el string es nulo usando el método de TextHelper
        if (TextHelper.isNull(number)) {
            return null;
        }
        try {
            return Double.parseDouble(number); // Convertir a double
        } catch (NumberFormatException e) {
            LOGGER.warn("Valor inválido para conversión numérica: {}", number, e);
            return null; // Si no es un número válido, retorna null.
        }
    }

    // Método para validar si un número es mayor que otro
    public static boolean isGreaterThan(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false; // Si uno de los números es nulo, no se puede comparar.
        }
        return num1 > num2;
    }

    // Método para validar si un número es menor que otro
    public static boolean isLessThan(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false;
        }
        return num1 < num2;
    }

    // Método para validar si dos números son iguales
    public static boolean areEqual(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false;
        }
        return Double.compare(num1, num2) == 0; // Comparación segura de doubles
    }

    // Método para validar si dos números son diferentes
    public static boolean areDifferent(Double num1, Double num2) {
        return !areEqual(num1, num2);
    }

    // Método para validar si un número pertenece a un rango
    public static boolean isInRange(Double number, Double lowerBound, boolean isLowerInclusive, 
                                    Double upperBound, boolean isUpperInclusive) {
        if (number == null || lowerBound == null || upperBound == null) {
            return false;
        }

        boolean isGreaterThanLower = isLowerInclusive ? number >= lowerBound : number > lowerBound;
        boolean isLessThanUpper = isUpperInclusive ? number <= upperBound : number < upperBound;

        return isGreaterThanLower && isLessThanUpper;
    }

    // Método para validar si un número es positivo
    public static boolean isPositive(Double number) {
        if (number == null) {
            return false;
        }
        return number > 0;
    }

    // Método para validar si un número es negativo
    public static boolean isNegative(Double number) {
        if (number == null) {
            return false;
        }
        return number < 0;
    }

}
