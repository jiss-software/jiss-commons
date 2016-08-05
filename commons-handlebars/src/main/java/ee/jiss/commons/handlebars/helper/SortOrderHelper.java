package ee.jiss.commons.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import ee.jiss.commons.mongo.OrderCondition;

import java.io.IOException;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SortOrderHelper implements Helper<String> {

    @Override
    public CharSequence apply(final String field, final Options options) throws IOException {
        int type = options.params.length == 1 ? parseInt(options.param(0).toString()) : 1;
        OrderCondition sortOrder = options.context.data("__sort_order");

        if (isNotBlank(field) && sortOrder != null) sortOrder.put(field, type);
        return null;
    }
}