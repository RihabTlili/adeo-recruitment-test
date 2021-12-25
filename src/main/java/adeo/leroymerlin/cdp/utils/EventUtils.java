package adeo.leroymerlin.cdp.utils;

/**
 * Utility class for the services.
 */
public final class EventUtils {

    /**
     * Concat a string to a number.
     *
     * @param title the initial string.
     * @param count the number to be concatenated to the string.
     *
     * @return string
     */
    public static String concatStringToNumber(String title, Integer count) {
        return String.format("%s [%d]", title, count);
    }
}
