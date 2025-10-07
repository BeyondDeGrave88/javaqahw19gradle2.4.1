package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.DashBoardPage;


import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.getAuthInfo;

public class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenCards() {
        var authInfo = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int initialBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int initialBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        int transferAmount = initialBalanceFirst / 2;

        TransferPage transferPage = dashBoardPage.selectCardToTransfer(secondCard.getTestId());
        dashBoardPage = transferPage.makeTransfer(firstCard.getNumber(), transferAmount);

        int finalBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int finalBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        Assertions.assertEquals(initialBalanceFirst - transferAmount, finalBalanceFirst);
        Assertions.assertEquals(initialBalanceSecond + transferAmount, finalBalanceSecond);

    }

    @Test
    void shouldNotTransferWhenAmountExceedsBalance() {
        var authInfo = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int initialBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int initialBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        int excessiveAmount = initialBalanceFirst + 1000;

        TransferPage transferPage = dashBoardPage.selectCardToTransfer(secondCard.getTestId());
        transferPage.makeInvalidTransfer(firstCard.getNumber(), excessiveAmount);

        Assertions.assertTrue(transferPage.isErrorNotificationVisibleWithText());

        // Возвращаемся на dashboard и проверяем, что балансы не изменились
        dashBoardPage = transferPage.cancelTransfer();

        int finalBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int finalBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        Assertions.assertEquals(initialBalanceFirst, finalBalanceFirst, "Баланс первой карты не должен измениться");
        Assertions.assertEquals(initialBalanceSecond, finalBalanceSecond, "Баланс второй карты не должен измениться");

    }

    @Test
    void shouldTransferAllBalance() {
        var authInfo = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int initialBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int initialBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        int transferAmount = initialBalanceFirst;

        TransferPage transferPage = dashBoardPage.selectCardToTransfer(secondCard.getTestId());
        dashBoardPage = transferPage.makeTransfer(firstCard.getNumber(), transferAmount);

        int finalBalanceFirst = dashBoardPage.getCardBalance(firstCard.getTestId());
        int finalBalanceSecond = dashBoardPage.getCardBalance(secondCard.getTestId());

        Assertions.assertEquals(0, finalBalanceFirst);
        Assertions.assertEquals(initialBalanceFirst + initialBalanceSecond, finalBalanceSecond);

    }
}

