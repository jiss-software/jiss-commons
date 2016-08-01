package ee.jiss.selenium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openqa.selenium.By.cssSelector;

public class Form {
    private static final String[] FIELDS = new String[] {"input", "textarea", "select"};
    private final WebElement element;
    private final Page page;

    public Form(WebElement element, Page page) {
        this.page = page;
        this.element = element;
    }

    public Form fill(Map<String, String> values) {
        return this.fill(values, values.keySet());
    }

    public Form fill(Map<String, String> values, Collection<String> fields) {
        fields.forEach(key -> this.fill(key, values.get(key)));
        return this;
    }

    public Form fill(String name, String value) {
        By by = nameSelector(name, FIELDS);
        try {
            WebElement element = this.element.findElement(by);

            if ("select".equalsIgnoreCase(element.getTagName())) {
                new Select(element).selectByValue(value);
            } else {
                element.clear();
                element.sendKeys(value);
            }
        } catch (NoSuchElementException exp) {
            fail("Element doesn't exists: " + by.toString());
        }

        return this;
    }

    public Form clear(Collection<String> values) {
        values.forEach(this::clear);
        return this;
    }

    public Form clear(String name) {
        By by = nameSelector(name, FIELDS);
        try {
            element.findElement(by).clear();
        } catch (NoSuchElementException exp) {
            fail("Element doesn't exists: " + by.toString());
        }

        return this;
    }

    public String collect(String name) {
        return element.findElement(nameSelector(name, FIELDS)).getAttribute("value");
    }

    public Map<String, String> collect() {
        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            element.findElements(cssSelector(join(",", (CharSequence[]) FIELDS)))
                    .forEach(elm -> result.put(elm.getAttribute("name"), elm.getAttribute("value")));

            if (result.values().stream().anyMatch(s -> s != null && ! s.isEmpty())) {
                break;
            }

            this.waiting(6, SECONDS);
        }

        return result;
    }

    public Form submit() {
        element.findElement(cssSelector("button[type='submit'],input[type='submit']")).click();
        return this;
    }

    public Collection<Element> findAll(String css) {
        return element.findElements(cssSelector(css)).stream().map(e -> new Element(e, this.page)).collect(toList());
    }

    public Element find(String css) {
        return new Element(element.findElement(cssSelector(css)), this.page);
    }

    private By nameSelector(String name, String... tags) {
        String attr = "[name='" + name + "']";
        return cssSelector(join(",", (CharSequence[]) stream(tags).map(tag -> tag + attr).toArray(String[]::new)));
    }

    public Form compare(Map<String, String> data) {
        Map<String, String> collected = this.collect();
        data.forEach((k, v) -> assertEquals("Field not equal: " + k, v, collected.get(k)));

        return this;
    }

    public Form waiting(long duration, TimeUnit unit) {
        try
        {
            unit.sleep(duration);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return this;
    }
}