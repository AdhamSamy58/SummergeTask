package com.booking.pages;

        import org.openqa.selenium.By;
        import org.openqa.selenium.JavascriptExecutor;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.PageFactory;
        import org.openqa.selenium.support.ui.ExpectedConditions;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import java.time.Duration;
        import java.util.List;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        PageFactory.initElements(driver, this);
    }

    public BookingDetailsPage selectHotel(String hotelName) {
        System.out.println("Searching for hotel: " + hotelName);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sr_property_block, .property-card")));
        } catch (Exception e) {
            System.err.println("Search results loading timeout: " + e.getMessage());
        }

        // Trying multiple strategies to find the hotel
        String[] xpathStrategies = {
                "//div[contains(@class, 'property-card')]//div[contains(@class, 'property-card__title')]//span[contains(text(), '" + hotelName + "')]",

                "//div[contains(@class, 'sr_property_block')]//span[contains(@class, 'sr-hotel__name') and contains(text(), '" + hotelName + "')]",

                "//div[contains(@class, 'hotel_name_link') or contains(@class, 'title') or contains(@class, 'name')]//span[contains(text(), '" + hotelName + "')]",

                "//*[contains(text(), '" + hotelName + "')]"
        };

        for (String xpathStrategy : xpathStrategies) {
            try {
                List<WebElement> hotelElements = driver.findElements(By.xpath(xpathStrategy));
                if (!hotelElements.isEmpty()) {
                    for (WebElement hotelElement : hotelElements) {
                        try {
                            WebElement container = getParentContainer(hotelElement);

                            WebElement availabilityButton = findAvailabilityButton(container);

                            if (availabilityButton != null) {
                                System.out.println("Found hotel and availability button. Clicking...");

                                JavascriptExecutor js = (JavascriptExecutor) driver;
                                js.executeScript("arguments[0].click();", availabilityButton);

                                String currentUrl = driver.getCurrentUrl();
                                try {
                                    for (int i = 0; i < 10; i++) {
                                        if (driver.getWindowHandles().size() > 1) {
                                            break;
                                        }

                                        if (!driver.getCurrentUrl().equals(currentUrl)) {
                                            break;
                                        }

                                        Thread.sleep(500);
                                    }
                                } catch (InterruptedException e) {}

                                String originalHandle = driver.getWindowHandle();
                                for (String handle : driver.getWindowHandles()) {
                                    if (!handle.equals(originalHandle)) {
                                        driver.switchTo().window(handle);
                                        break;
                                    }
                                }

                                return new BookingDetailsPage(driver);
                            }
                        } catch (Exception e) {
                            System.err.println("Error clicking hotel element: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Strategy failed: " + e.getMessage());
            }
        }

        try {
            WebElement nextPageButton = driver.findElement(By.cssSelector("[aria-label='Next page']"));
            if (nextPageButton.isEnabled()) {
                System.out.println("Hotel not found on current page. Trying next page...");
                nextPageButton.click();

                Thread.sleep(3000);

                return selectHotel(hotelName);
            }
        } catch (Exception e) {
            System.err.println("Error with pagination: " + e.getMessage());
        }

        throw new RuntimeException("Hotel '" + hotelName + "' not found in search results");
    }

    private WebElement getParentContainer(WebElement element) {
        try {
            return element.findElement(By.xpath("./ancestor::div[contains(@class, 'property-card') or contains(@class, 'sr_property_block')][1]"));
        } catch (Exception e) {
            try {
                return element.findElement(By.xpath("./ancestor::div[position()=3]"));
            } catch (Exception ex) {
                return element;
            }
        }
    }

    private WebElement findAvailabilityButton(WebElement container) {
        String[] buttonSelectors = {
                ".//a[contains(@class, 'b-button') and contains(text(), 'availability')]",
                ".//a[contains(@class, 'b-button') and contains(text(), 'Availability')]",
                ".//a[contains(@class, 'b-button') and contains(text(), 'See')]",
                ".//button[contains(text(), 'availability')]",
                ".//button[contains(text(), 'See')]",
                ".//a[contains(@href, 'hotel')]"
        };

        for (String selector : buttonSelectors) {
            try {
                List<WebElement> buttons = container.findElements(By.xpath(selector));
                if (!buttons.isEmpty()) {
                    return buttons.get(0);
                }
            } catch (Exception e) {
            }
        }

        return null;
    }
}