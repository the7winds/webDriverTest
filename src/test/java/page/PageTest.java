package page;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import page.components.ProductInfo;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PageTest {

    private Random random = new Random();
    private static WebDriver webDriver;
    private Logger logger = Logger.getLogger(getClass().getName());

    @BeforeClass
    public static void launch() {
        webDriver = new FirefoxDriver();
    }

    @AfterClass
    public static void quit() {
        webDriver.quit();
        webDriver = null;
    }

    public void marketTestScript(Page page, int min, int max, String brand) {
        page.setBrand(brand);
        page.waitSearchResultsUpdate();
        page.setMinPrice(min);
        page.waitSearchResultsUpdate();
        page.setMaxPrice(max);
        page.waitSearchResultsUpdate();

        logger.info(String.format("%s %d %d\n", brand, min, max));
        page.getProductsOnPage()
                .forEach(i -> logger.info(String.format("%s %d %d\n", i.getTitle(), i.getMinPrice(), i.getMaxPrice())));

        List<ProductInfo> searchResult = page.getProductsOnPage();
        long incorrectPrice = searchResult
                .stream()
                .filter(i -> (max < i.getMinPrice() ||  i.getMaxPrice() < min))
                .count();

        assertEquals(0, incorrectPrice);

        long incorrectTitle = searchResult
                .stream()
                .filter(i -> !i.getTitle().toLowerCase().contains(brand.toLowerCase()))
                .count();

        assertTrue(incorrectTitle <= searchResult.size() / 2);
    }

    @Test
    public void marketRandomTest() {
        Page page = new Page(webDriver);
        List<String> brands = page.availableBrands();

        String brand = brands.get(Math.abs(random.nextInt()) % brands.size());

        int min = Math.abs(random.nextInt()) % 50000;
        int max = min + Math.abs(random.nextInt()) % 50000;

        marketTestScript(page, min, max, brand);
    }

    @Test
    public void marketAveragePriceTests() {
        Page page = new Page(webDriver);
        String brand = "GoPro";
        int min = 16000;
        int max = 20000;

        marketTestScript(page, min, max, brand);
    }

    @Test
    public void marketLimitPriceTests() {
        Page page = new Page(webDriver);
        String brand = "GoPro";
        int min = 160000;
        int max = 200000;

        marketTestScript(page, min, max, brand);
    }
}
