package com.booking.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReservationPage {
    private WebDriver driver;
    private By hotelName = By.className("bp_hotel_name_title");

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHotelName() {
        return driver.findElement(hotelName).getText();
    }
}
