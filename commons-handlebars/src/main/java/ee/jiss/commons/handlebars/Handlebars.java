package ee.jiss.commons.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.ValueResolver;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import ee.jiss.commons.handlebars.helper.*;
import org.slf4j.Logger;

import java.io.IOException;

import static com.github.jknack.handlebars.Context.newBuilder;
import static org.slf4j.LoggerFactory.getLogger;

public class Handlebars {
    private final Logger logger = getLogger(this.getClass());

    private static final ValueResolver[] RESOLVERS = {
            MapValueResolver.INSTANCE,
            JavaBeanValueResolver.INSTANCE,
            FieldValueResolver.INSTANCE,
            MethodValueResolver.INSTANCE
    };

    private final com.github.jknack.handlebars.Handlebars engine;

    public Handlebars() {
        this.engine = new com.github.jknack.handlebars.Handlebars();

        engine.registerHelper("compare", new CompareHelper());
        engine.registerHelper("condition", new ConditionHelper());
        engine.registerHelper("formatDate", new DateFormatHelper());
        engine.registerHelper("eval", new EvalHelper());
        engine.registerHelper("ifEqual", new IfEqualHelper());
        engine.registerHelper("ifNull", new IfNullHelper());
        engine.registerHelper("inRange", new RangeHelper());
        engine.registerHelper("set", new SetHelper());
    }

    public Handlebars registerHelper(String name, Helper<?> helper) {
        engine.registerHelper(name, helper);
        return this;
    }

    public String compile(String markup, Object data) {
        try {
            Context context = newBuilder(data).resolver(RESOLVERS).build();
            return this.prepare(markup).apply(context);
        } catch (IOException e) {
            this.logger.error("Can't compile template.", e);
            return null;
        }
    }

    public String compile(String markup, Context data) {
        try {
            Context context = newBuilder(data).resolver(RESOLVERS).build();
            return this.prepare(markup).apply(context);
        } catch (IOException e) {
            this.logger.error("Can't compile template.", e);
            return null;
        }
    }

    public Template prepare(String markup) {
        try {
            return this.engine.compileInline(markup);
        } catch (IOException e) {
            this.logger.error("Can't prepare template.", e);
            return null;
        }
    }
}
