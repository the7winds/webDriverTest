package page.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;


public class Filter {
    private static final By thisLocator = By.cssSelector(".n-filter-panel-aside");
    private static final By filterBlockLocator = By.cssSelector(".n-filter-block_type_normal");
    private static final By filterBlockHeaderLocator = By.cssSelector(".n-filter-block__header");
    private static final By filterBlockItemLocator = By.cssSelector(".n-filter-block__item");
    private static final By filterBlockItemLabelLocator = By.cssSelector(".checkbox__label");
    private static final By filterBlockPriceRangeLocator = By.cssSelector(".n-filter-block__range");
    private static final By filterBlockPriceFrom = By.cssSelector(".input_price_from");
    private static final By filterBlockPriceTo = By.cssSelector(".input_price_to");

    private final WebElement webElement;

    public Filter(WebDriver webDriver) {
        webElement = webDriver.findElement(thisLocator);
    }

    private WebElement getProducerFilterElement() {
        return webElement.findElements(filterBlockLocator)
                .stream()
                .filter(we -> {
                    try {
                        return we.findElement(filterBlockHeaderLocator).getText().contains("Производитель");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .get();
    }

    private WebElement getPriceFilterElement() {
        return webElement.findElements(filterBlockLocator)
                .stream()
                .filter(we -> {
                    try {
                        return we.findElement(filterBlockHeaderLocator).getText().contains("Цена, руб.");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .get();
    }

    public List<String> availableBrands() {
        return getProducerFilterElement()
                .findElements(filterBlockItemLocator)
                .stream()
                .map(we -> we.findElement(filterBlockItemLabelLocator).getText())
                .collect(Collectors.toList());
    }

    public void setBrand(String brand) {
        getProducerFilterElement()
                .findElements(filterBlockItemLocator)
                .stream()
                .filter(we -> we.findElement(filterBlockItemLabelLocator).getText().equals(brand))
                .findFirst()
                .get()
                .findElement(By.tagName("input"))
                .click();
    }

    public WebElement getPriceRange() {
        return getPriceFilterElement().findElement(filterBlockPriceRangeLocator);
    }

    public void setMinPrice(int min) {
        WebElement minEl = getPriceRange().findElement(filterBlockPriceFrom)
                .findElement(By.tagName("input"));

        minEl.sendKeys(Integer.toString(min));
    }

    public void setMaxPrice(int max) {
        WebElement maxEl = getPriceRange().findElement(filterBlockPriceTo)
                .findElement(By.tagName("input"));

        maxEl.sendKeys(Integer.toString(max));
    }
}
