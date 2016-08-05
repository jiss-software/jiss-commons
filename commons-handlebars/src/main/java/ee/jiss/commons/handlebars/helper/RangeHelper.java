package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public class RangeHelper implements Helper<Integer> {
    @Override
    public CharSequence apply(final Integer from, final Options options) throws IOException {
        final Integer till = options.param(0);

        if (till == null) return null;

        final StringBuilder out = new StringBuilder();
        for (int i = from; i <= till; i++) out.append(options.fn(i));

        return out;
    }
}

