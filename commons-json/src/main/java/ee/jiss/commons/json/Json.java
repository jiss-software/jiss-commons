package ee.jiss.commons.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.InputStream;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.core.Version.unknownVersion;
import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static ee.jiss.commons.lang.ExceptionUtils.wrap;

public class Json {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final SimpleModule simpleModule = new SimpleModule("core.json", unknownVersion());

    static {
        MAPPER.setSerializationInclusion(NON_NULL);

        MAPPER.registerModule(simpleModule);

        MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);
        MAPPER.configure(USE_BIG_DECIMAL_FOR_FLOATS, true);
        MAPPER.configure(USE_BIG_INTEGER_FOR_INTS, true);

        MAPPER.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        MAPPER.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        MAPPER.configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    public static String asJson(Object object) {
        return wrap(() -> MAPPER.writeValueAsString(object));
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return wrap(() -> MAPPER.reader(type).readValue(json));
    }

    public static Map fromJson(String json) {
        return wrap(() -> MAPPER.reader(Map.class).readValue(json));
    }

    public static <T> T fromJsonStream(InputStream json, Class<T> type) {
        return wrap(() -> MAPPER.reader(type).readValue(json));
    }

    public static Map fromJsonStream(InputStream json) {
        return wrap(() -> MAPPER.reader(Map.class).readValue(json));
    }

    public static synchronized <T> void register(Class<T> t, JsonDeserializer<T> d, JsonSerializer<T> s) {
        simpleModule.addDeserializer(t, d);
        simpleModule.addSerializer(t, s);
    }
}