package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashBoardPage() {
        header.should(visible);
    }

    public void verifyPageIsVisible() {
        header.should(visible);
    }

    public int getCardBalance(String testId) {
        var cardElement = $("[data-test-id='" + testId + "']");
        var text = cardElement.text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String testId) {
        $("[data-test-id='" + testId + "'] button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }
}