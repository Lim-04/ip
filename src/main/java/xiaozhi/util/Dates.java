package xiaozhi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import xiaozhi.exception.XiaoZhiException;

/**
 * Parses and formats the dates XiaoZhi's deadlines and events carry.
 * <p>
 * Dates are always read (from user input or the save file) in ISO
 * {@code yyyy-mm-dd} form, and always shown to the user in a friendlier
 * {@code MMM dd yyyy} form, e.g. {@code Oct 15 2019}.
 */
public class Dates {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    private Dates() {
    }

    /**
     * Parses a date typed by the user, or read from the save file.
     * Leading and trailing whitespace is ignored.
     *
     * @param dateString The date text, expected in {@code yyyy-mm-dd} form.
     * @return The parsed date.
     * @throws XiaoZhiException If the text is not a valid {@code yyyy-mm-dd} date.
     */
    public static LocalDate parse(String dateString) throws XiaoZhiException {
        try {
            return LocalDate.parse(dateString.trim());
        } catch (DateTimeParseException e) {
            throw new XiaoZhiException(
                    "\"" + dateString + "\" isn't a valid date. Please use yyyy-mm-dd, e.g. 2019-10-15.");
        }
    }

    /**
     * Formats a date for display to the user, e.g. {@code "Oct 15 2019"}.
     *
     * @param date The date to format.
     * @return The date formatted for display.
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
