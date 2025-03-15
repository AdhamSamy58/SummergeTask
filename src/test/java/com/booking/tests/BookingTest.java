package com.booking.tests;

import com.booking.pages.BookingDetailsPage;
import com.booking.pages.HomePage;
import com.booking.pages.ReservationPage;
import com.booking.pages.SearchResultsPage;
import com.booking.utils.ExcelUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;
import org.testng.asserts.SoftAssert;

public class BookingTest {
    private WebDriver driver;
    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private BookingDetailsPage bookingDetailsPage;
    private ReservationPage reservationPage;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();
        driver.get("https://www.booking.com");
        homePage = new HomePage(driver);
    }

    @DataProvider(name = "bookingData")
    public Object[][] getBookingData() throws Exception {
        return ExcelUtils.getData("src/test/resources/testdata.xlsx", "Sheet1");
    }

    @Test(dataProvider = "bookingData")
    public void testBookingProcess(String location, String checkIn, String checkOut) throws InterruptedException {
        searchResultsPage = homePage.searchHotel(location, checkIn, checkOut);
        bookingDetailsPage = searchResultsPage.selectHotel("Tolip Hotel Alexandria");
        reservationPage = bookingDetailsPage.selectRoomAndReserve();

        // Assertions
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(reservationPage.getHotelName(), "Tolip Hotel Alexandria", "Hotel name mismatch");
//        softAssert.assertEquals(bookingDetailsPage.getDisplayedCheckInDate(), checkOut, "Check-in date mismatch");
//        softAssert.assertEquals(bookingDetailsPage.getDisplayedCheckOutDate(), checkOut, "Check-out date mismatch");
        softAssert.assertAll();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
