package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class DateTimeSerializer
        extends StdScalarSerializer<DateTime> {

    private static final DateTimeFormatter FORMATTER = forPattern("dd.MM.yyyy HH:mm");

    public DateTimeSerializer() {

        super(DateTime.class);
    }

    @Override
    public void serialize(final DateTime value, final JsonGenerator jg, final SerializerProvider provider)
            throws IOException {

        jg.writeString(FORMATTER.print(value));
    }
}