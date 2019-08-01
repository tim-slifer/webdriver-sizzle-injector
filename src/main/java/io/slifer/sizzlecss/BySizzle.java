/*
Copyright (c) 2019 Tim Slifer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
package io.slifer.sizzlecss;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import java.io.Serializable;
import java.util.List;

/**
 * Extends the By class to enable the use of Sizzle CSS Selectors.
 *
 * @author Tim Slifer
 */
public abstract class BySizzle extends By {
    
    /**
     * Find elements via the injected Sizzle CSS engine.
     *
     * @param sizzleCssSelector CSS expression.
     *
     * @return A By which locates elements by Sizzle CSS.
     */
    public static By css(final String sizzleCssSelector) {
        if (sizzleCssSelector == null) {
            throw new IllegalArgumentException("The selector cannot be null.");
        }
        return new BySizzleCssSelector(sizzleCssSelector);
    }
    
    public static class BySizzleCssSelector extends By implements Serializable {
        
        private String selector;
        private String sizzleScript;
        
        public BySizzleCssSelector(String selector) {
            this.selector = selector;
            this.sizzleScript = SizzleScript.getInstance().getScript();
        }
        
        @Override
        public WebElement findElement(SearchContext context) {
            WebDriver driver = getWebDriver(context);
            injectSizzleIfNeeded(driver);
            List elements = runSizzle(driver);
            
            if (elements.size() > 0) {
                return (WebElement) elements.get(0);
            }
            else {
                throw new NoSuchElementException("Unable to locate element: [" + selector + "]");
            }
        }
        
        @Override
        @SuppressWarnings ({"unchecked"}) // Yeah, I don't like this either.
        public List<WebElement> findElements(SearchContext context) {
            WebDriver driver = getWebDriver(context);
            injectSizzleIfNeeded(driver);
            
            return runSizzle(driver);
        }
        
        @Override
        public String toString() {
            return "BySizzle.css: [" + selector + "]";
        }
        
        private WebDriver getWebDriver(SearchContext context) {
            if (context instanceof WebDriver) {
                return (WebDriver) context;
            }
            
            if (context instanceof WrapsDriver) {
                return ((WrapsDriver) context).getWrappedDriver();
            }
            
            throw new IllegalStateException("WebDriver instance not found on SearchContext.");
        }
        
        private void injectSizzleIfNeeded(WebDriver driver) {
            if (!isSizzleLoaded(driver)) {
                ((JavascriptExecutor) driver).executeScript(sizzleScript);
            }
        }
        
        private Boolean isSizzleLoaded(WebDriver driver) {
            try {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return Sizzle() != null");
            }
            catch (WebDriverException e) {
                return false;
            }
        }
        
        private List runSizzle(WebDriver driver) {
            return (List) ((JavascriptExecutor) driver).executeScript("return Sizzle(\"" + selector + "\")");
        }
    }
}
