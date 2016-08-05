package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class SetHelper implements Helper<String> {
    @Override
    public CharSequence apply(final String variableName, final Options options) throws IOException {
        final CharSequence finalValue = options.apply(options.fn);
        options.context.data(variableName, finalValue.toString().trim());

        return null;
    }
}