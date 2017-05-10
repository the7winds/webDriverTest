package page.components;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ProductInfo {

    private static final By thisLocator = By.cssSelector(".n-snippet-card");
    private static final By contentLocator = By.cssSelector(".snippet-card__content");
    private static final By headerLocator = By.cssSelector(".snippet-card__header-text");
    private static final By minPriceLocator = By.cssSelector(".snippet-card__price");
    private static final By maxPriceLocator = By.cssSelector(".snippet-card__subprice > .price");
    private final WebElement webElement;
    private String title = null;
    private int minPrice = -1;
    private int maxPrice = -1;

    public ProductInfo(WebElement webElement) {
        this.webElement = webElement;

    }

    public static By getLocator() {
        return thisLocator;
    }

    public String getTitle() {
        if (title == null) {
            title = webElement.findElement(contentLocator)
                    .findElement(headerLocator)
                    .getText();
        }

        return title;
    }

    private int extractInt(String s) {
        return Integer.valueOf(s.chars()
                .filter(Character::isDigit)
                .mapToObj(x -> Character.toString((char) x))
                .reduce("0", String::concat));
    }

    public int getMinPrice() {
        if (minPrice == -1) {
            String priceTxt = webElement.findElement(minPriceLocator).getText();
            minPrice = extractInt(priceTxt);
        }

        return minPrice;
    }

    public int getMaxPrice() {
        if (maxPrice == -1) {
            try {
                maxPrice = extractInt(webElement.findElement(maxPriceLocator).getText());
            } catch (Exception e) {
                // ignore
            }

            if (maxPrice < 0) {
                maxPrice = getMinPrice();
            }
        }

        return maxPrice;
    }
}
