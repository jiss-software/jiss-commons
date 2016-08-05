package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class DateSerializer extends StdScalarSerializer<Date> {
    private static final DateTimeFormatter FORMATTER = forPattern("dd.MM.yyyy");

    public DateSerializer() {

        super(Date.class);
    }

    @Override
    public void serialize(final Date value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {

        jgen.writeString(FORMATTER.print(new LocalDate(value)));
    }
}