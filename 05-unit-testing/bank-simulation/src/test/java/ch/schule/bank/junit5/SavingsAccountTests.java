package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SavingsAccountTests {

    private SavingsAccount account;

    @BeforeEach
    void setUp() {
        account = new SavingsAccount("S-1000");
    }

    @Test
    void depositIncreasesBalance() {
        boolean ok = account.deposit(1, 500);

        assertTrue(ok);
        assertEquals(500, account.getBalance());
    }

    @Test
    void withdrawMoreThanBalanceIsRejected() {
        account.deposit(1, 100);

        boolean ok = account.withdraw(2, 150);

        assertFalse(ok);
        assertEquals(100, account.getBalance());
    }

    @Test
    void withdrawExactBalanceIsAllowed() {
        account.deposit(1, 100);

        boolean ok = account.withdraw(2, 100);

        assertTrue(ok);
        assertEquals(0, account.getBalance());
    }
}
