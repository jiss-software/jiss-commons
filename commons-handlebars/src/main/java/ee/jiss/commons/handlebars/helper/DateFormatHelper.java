package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DateFormatHelper implements Helper<Object> {
    private static final String FORMAT_KEY = "format";
    // TODO: Get for this locale
    private static final String DEFAULT_DATE_FORMAT = "yyyy-dd-mm";

    @Override
    public CharSequence apply(final Object date, final Options options) throws IOException {
        if (date == null || options == null) {
            // TODO: Show notify about problems (should be logged and shown)
            return "";
        }

        final String format = (String) options.hash.getOrDefault(FORMAT_KEY, DEFAULT_DATE_FORMAT);

        if (Date.class.isInstance(date)) return new SimpleDateFormat(format).format((Date) date);
        if (DateTime.class.isInstance(date)) return ((DateTime) date).toString(format);
        if (LocalTime.class.isInstance(date)) return ((LocalTime) date).toString(format);

        // TODO: Notify about wrong usage;
        return EMPTY;
    }
}

