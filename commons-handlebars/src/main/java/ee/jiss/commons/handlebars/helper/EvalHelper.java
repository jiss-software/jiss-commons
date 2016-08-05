package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

import static com.github.jknack.handlebars.Handlebars.SafeString;

public class EvalHelper implements Helper<Object> {
    private static final String VALUE_KEY = "value";

    @Override
    public CharSequence apply(final Object context, final Options options) throws IOException {
        return new SafeString(options.handlebars.compileInline((String) options.hash.get(VALUE_KEY)).apply(context));
    }
}

