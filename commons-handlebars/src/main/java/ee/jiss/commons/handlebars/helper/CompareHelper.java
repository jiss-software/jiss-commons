package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class CompareHelper implements Helper<Integer> {
    private static final String DEFAULT_OPERATOR = "==";
    public static final String OPERATOR_KEY = "operator";

    @Override
    public CharSequence apply(final Integer lv, final Options options) throws IOException {
        final Integer rv = options.param(0);

        if (lv == null || rv == null || options == null) {
            // TODO: Show notify about problems (should be logged and shown)
            return "";
        }

        final boolean result;
        switch ((String) options.hash.getOrDefault(OPERATOR_KEY, DEFAULT_OPERATOR)) {
            case ">":
                result = lv > rv;
                break;
            case ">=":
                result = lv >= rv;
                break;
            case "<":
                result = lv < rv;
                break;
            case "<=":
                result = lv <= rv;
                break;
            case "==":
                result = lv.equals(rv);
                break;
            case "!=":
                result = ! lv.equals(rv);
                break;
            default:
                result = false;
                // TODO: Notify that wrong usage
        }

        if (result) return options.fn(options.context);

        return options.inverse(options.context);
      }
}