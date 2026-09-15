package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalaryAccountTests {

    @Test
    void withdrawBelowCreditLimitIsRejected() {
        SalaryAccount account = new SalaryAccount("P-1000", -500);
        account.deposit(1, 100);

        boolean ok = account.withdraw(2, 700);

        assertFalse(ok);
        assertEquals(100, account.getBalance());
    }

    @Test
    void withdrawExactlyAtCreditLimitIsAllowed() {
        SalaryAccount account = new SalaryAccount("P-1000", -500);
        account.deposit(1, 100);

        boolean ok = account.withdraw(2, 600);

        assertTrue(ok);
        assertEquals(-500, account.getBalance());
    }
}
