package ee.jiss.selenium.util;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Consumer;

import static ee.jiss.commons.lang.CheckUtils.isNotEmptyString;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.fail;
import static org.openqa.selenium.By.cssSelector;

public class Table {
    private final WebElement element;
    private final Page page;

    public Table(WebElement element, Page page) {
        this.page = page;
        this.element = element;
    }

    public Table times(int qty, Consumer<Table> action) {
        for (int i = 0; i < qty; i++) action.accept(this);
        return this;
    }

    public List<String> headers() {
        try {
            page.waiting(d -> ! d.findElements(cssSelector("thead tr th")).isEmpty(), null);
        } catch (Throwable exp) {
            fail("Headers not found");
        }

        return element.findElements(cssSelector("thead tr th")).stream().map(WebElement::getText).collect(toList());
    }

    public void assertHeaders(String... headers) {
        int i = 0;
        List<String> labels = headers();

        if (labels.size() < headers.length)
            fail("Wrong length of headers - actual = " + labels.size() + " but expected = " + headers.length);

        for (; i < headers.length; i++) {
            if (! headers[i].equalsIgnoreCase(labels.get(i))) {
                fail("Wrong header name on column nr " + i + ". " + headers[i] + " != " + labels.get(i));
            }
        }

        if (labels.size() > i && isNotEmptyString(labels.get(i))) {
            fail("Wrong header length, out of index on " + i + " - " + labels.get(i));
        }
    }
    public Table find(String css) {
        return new Table(element.findElement(cssSelector(css)), this.page);
    }
}
