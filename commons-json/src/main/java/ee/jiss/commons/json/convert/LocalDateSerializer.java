package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class LocalDateSerializer
        extends StdScalarSerializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = forPattern("dd.MM.yyyy");

    public LocalDateSerializer() {

        super(LocalDate.class);
    }

    @Override
    public void serialize(final LocalDate value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {

        jgen.writeString(FORMATTER.print(value));
    }
}