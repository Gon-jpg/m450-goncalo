package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Bank;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTests {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void createSavingsAccountReturnsIdWithPrefixS() {
        String id = bank.createSavingsAccount();

        assertTrue(id.startsWith("S-"));
    }

    @Test
    void createPromoYouthSavingsAccountReturnsIdWithPrefixY() {
        String id = bank.createPromoYouthSavingsAccount();

        assertTrue(id.startsWith("Y-"));
    }

    @Test
    void createSalaryAccountWithValidCreditLimitReturnsIdWithPrefixP() {
        String id = bank.createSalaryAccount(-500);

        assertNotNull(id);
        assertTrue(id.startsWith("P-"));
    }

    @Test
    void createSalaryAccountWithPositiveCreditLimitIsRejected() {
        String id = bank.createSalaryAccount(100);

        assertNull(id);
    }

    @Test
    void depositToValidIdUpdatesBalance() {
        String id = bank.createSavingsAccount();

        boolean ok = bank.deposit(id, 1, 300);

        assertTrue(ok);
        assertEquals(300, bank.getBalance(id));
    }

    @Test
    void withdrawFromValidIdUpdatesBalance() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 300);

        boolean ok = bank.withdraw(id, 2, 100);

        assertTrue(ok);
        assertEquals(200, bank.getBalance(id));
    }

    @Test
    void depositToUnknownIdReturnsFalse() {
        boolean ok = bank.deposit("does-not-exist", 1, 300);

        assertFalse(ok);
    }

    @Test
    void withdrawFromUnknownIdReturnsFalse() {
        boolean ok = bank.withdraw("does-not-exist", 1, 100);

        assertFalse(ok);
    }

    @Test
    void getBalanceWithNoArgsReturnsNegativeSumOfAllAccounts() {
        String idA = bank.createSavingsAccount();
        String idB = bank.createSavingsAccount();
        bank.deposit(idA, 1, 1000);
        bank.deposit(idB, 1, 500);

        long total = bank.getBalance();

        assertEquals(-1500, total);
    }

    @Test
    void getBalanceWithUnknownIdReturnsZero() {
        long balance = bank.getBalance("does-not-exist");

        assertEquals(0, balance);
    }

    @Test
    void printForKnownIdDoesNotThrow() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 20);

        assertDoesNotThrow(() -> bank.print(id));
    }

    @Test
    void printForUnknownIdIsNoOp() {
        assertDoesNotThrow(() -> bank.print("does-not-exist"));
    }

    @Test
    void printByMonthForKnownIdDoesNotThrow() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 20);

        assertDoesNotThrow(() -> bank.print(id, 1970, 1));
    }

    @Test
    void printByMonthForUnknownIdIsNoOp() {
        assertDoesNotThrow(() -> bank.print("does-not-exist", 1970, 1));
    }

    @Test
    void printTop5DoesNotThrowWithSeveralAccounts() {
        for (int i = 0; i < 7; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, (i + 1) * 100);
        }

        assertDoesNotThrow(() -> bank.printTop5());
    }

    @Test
    void printBottom5DoesNotThrowWithSeveralAccounts() {
        for (int i = 0; i < 7; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, (i + 1) * 100);
        }

        assertDoesNotThrow(() -> bank.printBottom5());
    }

    @Test
    void printTop5AndBottom5DoNotThrowOnEmptyBank() {
        assertDoesNotThrow(() -> bank.printTop5());
        assertDoesNotThrow(() -> bank.printBottom5());
    }

    @Test
    void getSetAccountStoresValue() {
        Account a = new SavingsAccount("S-777");

        bank.setAccount(a);

        assertSame(a, bank.getAccount());
    }

    @Test
    void getAccountDefaultsToNull() {
        assertNull(bank.getAccount());
    }
}
