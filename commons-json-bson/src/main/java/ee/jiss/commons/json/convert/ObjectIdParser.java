package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.bson.types.ObjectId;
import org.joda.time.LocalDate;

import java.io.IOException;

import static ee.jiss.commons.lang.CheckUtils.isEmptyString;

public class ObjectIdParser extends StdScalarDeserializer<ObjectId> {
    public ObjectIdParser() {
        super(LocalDate.class);
    }

    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String text = jp.getText();
        return isEmptyString(text) ? null : new ObjectId(text);
    }
}