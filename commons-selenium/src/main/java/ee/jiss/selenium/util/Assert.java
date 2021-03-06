package ee.jiss.selenium.util;

import org.openqa.selenium.WebElement;

import static org.junit.Assert.fail;
import static org.openqa.selenium.By.cssSelector;

public class Assert {
    private final Page page;
    private final String selector;

    public Assert(Page page, String selector)
    {
        this.page = page;
        this.selector = selector;
    }

    public Assert exists(String message) {
        try {
            page.waiting(d -> ! d.findElements(cssSelector(selector)).isEmpty(), null);
        } catch (Throwable exp) {
            fail(selector + ": " + message);
        }

        return this;
    }

    public Assert displayed(String message) {
        try {
            page.waiting(d -> d.findElements(cssSelector(selector)).stream().anyMatch(WebElement::isDisplayed), null);
        } catch (Throwable exp) {
            fail(selector + ": " + message);
        }

        return this;
    }

    public Assert notExists(String message) {
        if (page.wd.findElements(cssSelector(selector)).isEmpty()) return this;
        fail("Css selector - " + selector + ": " + message);
        return this;
    }

    public Element element() {
        return page.element(selector);
    }
}
