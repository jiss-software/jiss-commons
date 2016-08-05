package ee.jiss.commons.json.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdSerializer extends StdScalarSerializer<ObjectId> {
    public ObjectIdSerializer() {
        super(ObjectId.class);
    }

    @Override
    public void serialize(ObjectId value, JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeString(value.toString());
    }
}