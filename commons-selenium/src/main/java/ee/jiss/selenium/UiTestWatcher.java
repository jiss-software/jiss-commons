package ee.jiss.selenium;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.openqa.selenium.OutputType.FILE;

public class UiTestWatcher extends TestWatcher {
    private WebDriver wd;

    public UiTestWatcher(WebDriver wd) {
        this.wd = wd;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        File destination = Paths.get(getUserDirectoryPath(), "TestFail-" + new Date() + ".png").toFile();

        try {
            FileUtils.copyFile(((TakesScreenshot) wd).getScreenshotAs(FILE), destination);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
