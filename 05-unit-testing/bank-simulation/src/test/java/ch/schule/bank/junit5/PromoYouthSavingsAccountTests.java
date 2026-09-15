package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromoYouthSavingsAccountTests {

    @Test
    void deposit100AddsOnePercentBonus() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");

        boolean ok = account.deposit(1, 100);

        assertTrue(ok);
        assertEquals(101, account.getBalance());
    }

    @Test
    void depositUnder100RoundsBonusDownToZero() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");

        boolean ok = account.deposit(1, 50);

        assertTrue(ok);
        assertEquals(50, account.getBalance());
    }
}
