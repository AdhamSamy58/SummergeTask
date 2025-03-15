package com.booking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;


public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchBox = By.xpath("//input[@id=':rh:']");
    private By checkInDate = By.xpath("//span[normalize-space()='Check-in date']");
    private By checkOutDate = By.xpath("//span[normalize-space()='Check-out date']");
    private By searchButton = By.xpath("//span[normalize-space()='Search']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public SearchResultsPage searchHotel(String location, String checkIn, String checkOut) throws InterruptedException {
        driver.findElement(searchBox).sendKeys(location);
        Thread.sleep(1000);
        driver.findElement(checkInDate).click();
        driver.findElement(By.xpath("//span[@aria-label='1 April 2025']")).click();
        driver.findElement(By.xpath("//span[@aria-label='14 April 2025']")).click();
        driver.findElement(searchButton).click();
        return new SearchResultsPage(driver);
    }
}