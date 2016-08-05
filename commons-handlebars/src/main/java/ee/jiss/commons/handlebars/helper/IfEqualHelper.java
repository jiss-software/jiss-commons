package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class IfEqualHelper implements Helper<Object> {
    @Override
    public CharSequence apply(final Object first, final Options options) throws IOException {
        if (options == null) return "";

        final Object second = options.param(0);

        if (first == null && second == null || first != null && first.equals(second))
            return options.fn(options.context);

        return options.inverse(options.context);
    }
}

