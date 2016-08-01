package ee.jiss.selenium.util;

import org.openqa.selenium.WebElement;

import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.By.cssSelector;

public class Element {
    private final WebElement element;
    private final Page page;

    public Element(WebElement element, Page page) {
        this.page = page;
        this.element = element;
    }

    public Element times(int qty, Consumer<Element> action) {
        for (int i = 0; i < qty; i++) action.accept(this);
        return this;
    }

    public Element fill(String value) {
        this.element.sendKeys(value);
        return this;
    }

    public Element clear() {
        element.clear();
        return this;
    }

    public Element click() {
        int i = 0;

        while (true) {
            try {
                this.element.click();
                return this;
            } catch (Exception exp) {
                if (i > 3) {
                    throw exp;
                }

                this.page.waiting(i + 1, SECONDS);
                i++;
            }
        }
    }

    public Element find(String css) {
        return new Element(element.findElement(cssSelector(css)), this.page);
    }

    public String collect() {
        return element.getAttribute("value");
    }
}
