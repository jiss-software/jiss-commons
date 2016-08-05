package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class IfNullHelper implements Helper<Object> {
    @Override
    public CharSequence apply(final Object value, final Options options) throws IOException {
        if (value == null) return options.fn();
        return options.inverse();
    }
}

