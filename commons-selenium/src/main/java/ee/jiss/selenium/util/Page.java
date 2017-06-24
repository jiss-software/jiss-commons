package ee.jiss.selenium.util;

import com.google.common.base.Predicate;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ee.jiss.commons.lang.CheckUtils.isNotEmptyString;
import static java.util.Arrays.copyOfRange;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openqa.selenium.By.cssSelector;

public class Page {
    public final WebDriver wd = new FirefoxDriver();

    private final String host;

    protected Page(String host) {
        this.host = host;
        wd.manage().timeouts().implicitlyWait(3, SECONDS);
    }

    public static Page page(String host) {
        return new Page(host);
    }

    public Page get(String... path) {
        String _path = "";
        if (path.length > 0) _path = Paths.get(path[0], copyOfRange(path, 1, path.length)).toString();

        String location = host + _path;
        wd.get(location);

        waiting(driver -> {
            if (driver == null) throw new IllegalStateException();

            String currentUrl = driver.getCurrentUrl();
            return currentUrl.equalsIgnoreCase(location) || (currentUrl + "/").equalsIgnoreCase(location);
        }, "Failed to load page: " + _path);

        return this;
    }

    public Form form(String css) {
        waiting(driver -> ! driver.findElements(cssSelector(css)).isEmpty(), css);
        return new Form(wd.findElement(cssSelector("form" + css)), this);
    }

    public Element element(String css) {
        waiting(driver -> ! driver.findElements(cssSelector(css)).isEmpty(), css);
        return new Element(wd.findElement(cssSelector(css)), this);
    }

    public Table table(String css) {
        waiting(driver -> ! driver.findElements(cssSelector(css)).isEmpty(), css);
        return new Table(wd.findElement(cssSelector(css)), this);
    }

    public Element a(String link) {
        return element("a[href='" + link + "']");
    }

    public Element button(String css) {
        return element("button" + css + ",input" + css);
    }

    public String getHash() {
        return wd.getCurrentUrl().replaceAll(".*#", "");
    }

    public Page waiting(Predicate<WebDriver> predicate, String message) {
        try {
            new WebDriverWait(wd, 100).until(predicate);
        } catch (TimeoutException exp) {
            fail("Waiting for: " + message + " failed");
        }

        return this;
    }

    public Page waiting(long duration, TimeUnit unit) {
        try {
            unit.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    public Page waitForLoad() {
        new WebDriverWait(wd, 30).until((ExpectedCondition<Boolean>) wd ->
                    wd != null && ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));

        return this;
    }

    public Assert check(String css) {
        return new Assert(this, css);
    }

    public Page assertHash(String message, String hash) {
        for (int i = 0; i < 5; i++) {
            if (hash.equals(getHash())) break;
            waiting(500, MILLISECONDS);
        }

        assertEquals(message, hash, hash);

        return this;
    }

    public List<Map<String, ?>> errors() {
        String script = "return window.JSErrorCollector_errors ? window.JSErrorCollector_errors.pump() : []";
        return (List<Map<String, ?>>) ((JavascriptExecutor) wd).executeScript(script);
    }

    public void close() {
        wd.quit();

    }
}
