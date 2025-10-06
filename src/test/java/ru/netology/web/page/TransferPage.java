package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public TransferPage() {
        amountField.should(Condition.visible);
    }

    public DashBoardPage makeTransfer(String fromCardNumber, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCardNumber);
        transferButton.click();
        return new DashBoardPage();
    }

    public void makeInvalidTransfer(String fromCardNumber, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCardNumber);
        transferButton.click();
        amountField.shouldBe(Condition.visible);
    }

    public DashBoardPage cancelTransfer() {
        cancelButton.click();
        return new DashBoardPage();
    }

    public boolean isErrorVisible() {
        return errorNotification.is(Condition.visible);
    }
}