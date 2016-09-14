package crawley.james.BankAccount;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class BankAccountTest {

    Account openSavingsAccount = new Account(AccountType.SAVINGS, "John Williams", "1234");
    Account openCheckingAccount = new Account(AccountType.CHECKING, "John Williams", "1235");
    Account frozenAccount = new Account (AccountType.SAVINGS, "John Williams", "1236");
    Account closedAccount = new Account (AccountType.SAVINGS, "John Williams", "1233");
    String expectedList = "";

    @Test
    public void nameTest () {

        assertEquals("The name should be \"John Williams\"", "John Williams", openSavingsAccount.getName());

    }

    @Test
    public void interestTest () {

        assertEquals("The interest rate should be 3%.", 0.03, openSavingsAccount.getInterestRate(), 0);

    }

    @Test
    public void statusTest () {

        assertEquals("The status should be open.", AccountStatus.OPEN, openSavingsAccount.getAccountStatus());

    }

    @Test
    public void overdraftTest () {

        assertEquals("The protection status should be disabled.", OverdraftPrevention.DISABLED, openSavingsAccount.getOverdraftPrevention());

    }

    @Test
    public void withdrawalRecordTest () {


        openSavingsAccount.credit(100);
        openSavingsAccount.debit(25);
        openSavingsAccount.debit(17);
        openSavingsAccount.debit(40);
        openSavingsAccount.debit(18);

        expectedList = "Your recent withdrawals:\n" +
                "1234: -$18.0\n" +
                "1234: -$40.0\n" +
                "1234: -$17.0\n" +
                "1234: -$25.0\n";

        assertEquals("The withdrawal history should be 18, 40, 17, 25", expectedList, openSavingsAccount.getWithdrawalHistory());


    }

    @Test
    public void depositRecordTest () {

        openSavingsAccount.credit(25);
        openSavingsAccount.credit(17);
        openSavingsAccount.credit(40);
        openSavingsAccount.credit(18);

        expectedList = "Your recent deposits:\n" +
                "1234: +$18.0\n" +
                "1234: +$40.0\n" +
                "1234: +$17.0\n" +
                "1234: +$25.0\n";

        assertEquals("The deposit history should be 18, 40, 17, 25", expectedList, openSavingsAccount.getDepositHistory());

    }

    @Test
    public void transferRecordTest () {

        frozenAccount.setAccountStatus(AccountStatus.FREEZE);
        openSavingsAccount.credit(100);
        openSavingsAccount.transfer(openCheckingAccount, 20);
        openSavingsAccount.transfer(frozenAccount, 40);
        openSavingsAccount.transfer(openCheckingAccount, 40);
        openSavingsAccount.transfer(openCheckingAccount, 50);


        expectedList = "Your recent transfers:\n" +
                "1234 ---> 1235: $40.0\n" +
                "1234 ---> 1235: $20.0\n";

        assertEquals("The transfer history should be 40, 20", expectedList, openSavingsAccount.getTransferHistory());


    }

    @Test
    public void statusRecordTest () {

        openSavingsAccount.setAccountStatus(AccountStatus.FREEZE);
        openSavingsAccount.setAccountStatus(AccountStatus.OPEN);
        openSavingsAccount.setAccountStatus(AccountStatus.CLOSED);
        openSavingsAccount.setAccountStatus(AccountStatus.OPEN);

        //Constructor not called here so no initial status in log
        expectedList = "Your recent status updates:\n1234 status: CLOSED\n1234 status: OPEN\n1234 status: FREEZE\n";

        assertEquals("The status history should be CLOSED, OPEN, FREEZE", expectedList, openSavingsAccount.getStatusHistory());

    }

    @Test
    public void nameRecordTest () {

        openSavingsAccount.setName("John Billiams");
        openSavingsAccount.setName("John Shilliams");
        openSavingsAccount.setName("Bill Johnson");
        openSavingsAccount.setName("Will Sonjohn");

        //Constructor not called here so no initial status in log
        expectedList = "Your recent name changes:\n1234 name: Will Sonjohn\n1234 name: Bill Johnson\n1234 name: John Shilliams\n1234 name: John Billiams\n";

        assertEquals("The name changes should be \"Will Sonjohn\", \"Bill Johnson\", \"John Shilliams\" then \"John Billiams\".", expectedList, openSavingsAccount.getNameHistory());
    }

    @Test
    public void interestRecordTest () {

        openSavingsAccount.setInterestRate(0.052);
        openSavingsAccount.setInterestRate(0.023);
        openSavingsAccount.setInterestRate(0.067);
        openSavingsAccount.setInterestRate(0.058);

        //Constructor not called here so no initial status in log
        expectedList = "Your recent interest rate changes:\n1234 interest: 6%\n1234 interest: 7%\n1234 interest: 2%\n1234 interest: 5%\n";

        assertEquals("The interest rate changed should be 6%, 7%, 2%, 5%.", expectedList, openSavingsAccount.getInterestHistory());

    }

    @Test
    public void checkBalanceOnFrozenAccountTest () {

        frozenAccount.setAccountStatus(AccountStatus.FREEZE);
        assertEquals("The value should be inaccessible to frozen accounts.", null, frozenAccount.getBalance());

    }


    @Test
    public void debitApprovalTest () {

        openSavingsAccount.credit(50);
        openSavingsAccount.debit(20);
        assertEquals("The value should be $30", 30, openSavingsAccount.getBalance(), 0);

    }


    @Test
    public void debitDenialClosedTest () {

        closedAccount.setAccountStatus(AccountStatus.CLOSED);
        closedAccount.debit(30);
        assertEquals("The value should be $0", 0, closedAccount.getBalance(), 0);

    }

    @Test
    public void debitOverdraftApprovalTest () {

        openSavingsAccount.debit(40);
        assertEquals("The value should be $-10", -40, openSavingsAccount.getBalance(), 0);

    }

    @Test
    public void debitOverdraftDenialTest () {

        openSavingsAccount.credit(30);
        openSavingsAccount.setOverdraftPrevention(OverdraftPrevention.ENABLED);
        openSavingsAccount.debit(40);
        assertEquals("The value should be $30", 30, openSavingsAccount.getBalance(), 0);


    }

    @Test
    public void creditApprovalTest () {

        openSavingsAccount.credit(30);
        assertEquals("The value should be $30", 30, openSavingsAccount.getBalance(), 0);

    }

    @Test
    public void creditDenialClosedTest () {

        closedAccount.setAccountStatus(AccountStatus.CLOSED);
        closedAccount.credit(50);
        assertEquals("Closed accounts should have a balance of $0", 0, closedAccount.getBalance(), 0);

    }

    @Test
    public void transferApprovalTest () {

        openSavingsAccount.credit(50);
        openSavingsAccount.transfer(openCheckingAccount, 40);
        assertEquals("The checking account should receive $40", 40, openCheckingAccount.getBalance(), 0);

    }

    @Test
    public void transferDenialTest () {

        openCheckingAccount.credit(40);
        openSavingsAccount.transfer(openCheckingAccount, 20);
        assertEquals("Cannot overdraft on transfer", 40, openCheckingAccount.getBalance(), 0);

    }

    @Test
    public void changeNameTest () {

        openSavingsAccount.setName("John Billiams");
        assertEquals("Name should be changed to \"John Billiams\".", "John Billiams", openSavingsAccount.getName());

    }


    @Test
    public void changeNameDeniedTest () {

        closedAccount.setAccountStatus(AccountStatus.CLOSED);
        closedAccount.setName("John Crilliams");
        assertEquals("Cannot change name of a closed account.", "John Williams", closedAccount.getName());

    }

    @Test
    public void reopeningAClosedAccountTest () {

        closedAccount.setAccountStatus(AccountStatus.CLOSED);
        closedAccount.setAccountStatus(AccountStatus.OPEN);
        assertEquals("Cannot reopen a closed account.", AccountStatus.CLOSED, closedAccount.getAccountStatus());
    }

    @Test
    public void closingAccountWithBalanceTest () {
        openSavingsAccount.credit(50);
        openSavingsAccount.setAccountStatus(AccountStatus.CLOSED);
        assertEquals("Cannot close an account with a balance.", AccountStatus.OPEN, openSavingsAccount.getAccountStatus());

    }



}
