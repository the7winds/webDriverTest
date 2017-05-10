package page;

import org.apache.bcel.ExceptionConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.components.Filter;
import page.components.ProductInfo;

import java.util.List;
import java.util.stream.Collectors;


public class Page {
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;
    private final Filter filter;
    private static final String url = "https://market.yandex.ru/catalog/54761/list?hid=90635&text=%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D1%8B&local-offers-first=0&deliveryincluded=0&onstock=1";
    private final By searchResCounterLocator = By.cssSelector(".search-preciser__results-count");
    private static final String EmptyResultString = "0 результатов";

    public Page(WebDriver webDriver) {
        webDriver.get(url);
        this.webDriver = webDriver;
        this.webDriverWait = new WebDriverWait(webDriver, 10);
        this.filter = new Filter(webDriver);
    }

    public List<String> availableBrands() {
        return filter.availableBrands();
    }

    private boolean emptyResult() {
        return webDriver.findElement(searchResCounterLocator)
                .getText()
                .contains(EmptyResultString);
    }

    public void waitSearchResultsUpdate() {
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(ProductInfo.getLocator()));
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResCounterLocator));
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ProductInfo.getLocator()));
        } catch (TimeoutException e) {
            if (emptyResult()) {
                return;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void setBrand(String brand) {
        filter.setBrand(brand);
    }

    public List<ProductInfo> getProductsOnPage() {
        return webDriver.findElements(ProductInfo.getLocator())
                .stream()
                .map(ProductInfo::new)
                .collect(Collectors.toList());
    }

    public void setMinPrice(int min) {
        filter.setMinPrice(min);
    }

    public void setMaxPrice(int max) {
        filter.setMaxPrice(max);
    }
}

