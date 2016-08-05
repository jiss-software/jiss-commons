package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

import static ee.jiss.commons.lang.CheckUtils.isEmptyString;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class DateTimeParser
        extends StdScalarDeserializer<DateTime> {

    private static final DateTimeFormatter FORMATTER = forPattern("dd.MM.yyyy");

    public DateTimeParser() {

        super(DateTime.class);
    }

    @Override
    public DateTime deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {

        final String text = jp.getText();
        return isEmptyString(text) ? null : FORMATTER.parseDateTime(text);
    }
}