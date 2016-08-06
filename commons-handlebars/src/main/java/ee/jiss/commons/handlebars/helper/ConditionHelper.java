package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.Map;

import static ee.jiss.commons.lang.RegexUtils.pattern;
import static ee.jiss.commons.mongo.DBUtils.gt;
import static ee.jiss.commons.mongo.DBUtils.lt;
import static ee.jiss.commons.mongo.DBUtils.regex;
import static java.lang.Long.parseLong;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ConditionHelper implements Helper<String> {
    // TODO: Work 'and', 'or', 'xor' logic condition groups

    @Override
    public CharSequence apply(final String field, final Options options) throws IOException {
        final Object value = options.param(0);
        final String type = options.params.length == 2 ? options.param(1) : "";
        final Map<String, Object> conditions = options.context.data("__condition");

        if (isBlank(field) || value == null || isBlank(value.toString()) || conditions == null) return null;

        switch (type) {
            case "gt":
                conditions.put(field, gt(parseLong(value.toString())));
                break;
            case "lt":
                conditions.put(field, lt(parseLong(value.toString())));
                break;
            case "regex":
                conditions.put(field, regex(pattern(value.toString())));
                break;
            default:
                conditions.put(field, value);
        }

        return null;
    }
}