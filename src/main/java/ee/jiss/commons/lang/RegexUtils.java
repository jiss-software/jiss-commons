package ee.jiss.commons.lang;

import java.util.regex.Pattern;

import static ee.jiss.commons.lang.ExceptionUtils.ignore;
import static java.lang.Double.parseDouble;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class RegexUtils {
    public static Pattern pattern(String str) {
        return Pattern.compile(str);
    }

    public static Pattern dateValue(String str, String formatIn, String formatOut) {
        return ignore(() -> pattern(forPattern(formatIn).parseDateTime(str).toString(formatOut)));
    }

    public static Pattern dateValue(String str, String format) {
        return dateValue(str, format, format);
    }

    public static Double numberValue(String str) {
        str = str.replace(',', '.');
        if (! str.matches("\\+?-?\\d+(\\.\\d+)?")) return null;

        return parseDouble(str);
    }
}
