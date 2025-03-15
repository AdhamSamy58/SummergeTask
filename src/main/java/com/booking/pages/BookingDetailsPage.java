package com.booking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingDetailsPage {
    private static WebDriver driver;
    private By reserveButton = By.xpath("//button[@id='hp_book_now_button']//span[@class='bui-button__text'][normalize-space()='Reserve']");
    private By reserveButton2 = By.xpath("//span[@class='bui-button__text js-reservation-button__text']");

    private static By checkInDisplayed = By.xpath("//div[@class='ffb9c3d6a3 b3b8f00b52 c9a7790c31 e79e8c68d5 b237a13ee1']//span[@class='a8887b152e'][normalize-space()='Tue, Apr 1']");
    private static By checkOutDisplayed = By.cssSelector("//div[@class='ffb9c3d6a3 b3b8f00b52 c9a7790c31 e79e8c68d5 b237a13ee1']//span[@class='a8887b152e'][normalize-space()='Mon, Apr 14']");

    public BookingDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public ReservationPage selectRoomAndReserve() throws InterruptedException {
        driver.findElement(reserveButton).click();
        Thread.sleep(1000);
        driver.findElement(reserveButton2).click();

        return new ReservationPage(driver);
    }

    public static String getDisplayedCheckInDate() {
        return driver.findElement(checkInDisplayed).getText();
    }

    public static String getDisplayedCheckOutDate() {
        return driver.findElement(checkOutDisplayed).getText();
    }


}
