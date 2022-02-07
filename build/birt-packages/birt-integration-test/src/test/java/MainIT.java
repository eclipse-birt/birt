/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

import junit.framework.TestCase;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainIT extends TestCase {
    public void testExecute()
    throws Exception
    {
        System.setProperty("phantomjs.binary.path", System.getProperty("phantomjs.binary"));
        WebDriver driver = new PhantomJSDriver();
        
        // And now use this to visit birt report viewer
        driver.get("http://localhost:9999");

        // Check the title of the page
        assertEquals("Eclipse BIRT Home", driver.getTitle());
        
        // Click view exmaple button       
        WebElement element = driver.findElement(By.linkText("View Example"));
        element.click();

        // Wait until page loaded
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {

                // Check the title of loaded page
                assertEquals("BIRT Report Viewer", d.getTitle());


                // Check the success message
                assertTrue(d.getPageSource().contains("Congratulations!"));
                return true;
            }
        });

        //Close the browser
        driver.quit();    
    }

}
