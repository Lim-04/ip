package xiaozhi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import xiaozhi.exception.XiaoZhiException;

public class Dates {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    private Dates() {
    }

    // Parses a date typed by the user (or read from the save file), expected as yyyy-mm-dd
    public static LocalDate parse(String dateString) throws XiaoZhiException {
        try {
            return LocalDate.parse(dateString.trim());
        } catch (DateTimeParseException e) {
            throw new XiaoZhiException(
                    "\"" + dateString + "\" isn't a valid date. Please use yyyy-mm-dd, e.g. 2019-10-15.");
        }
    }

    // Formats a date for display, e.g. "Oct 15 2019"
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
