package crawley.james.BankAccount;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class BankAccountTest {

    Account account0 = new Account(AccountType.SAVINGS, "John Williams", "1234");
    Account account1 = new Account(AccountType.CHECKING, "John Williams", "1235");
    Account account2 = new Account (AccountType.SAVINGS, "John Williams", "1236");
    Account account3 = new Account (AccountType.SAVINGS, "John Williams", "1233");
    String expectedList = "";

    @Test
    public void nameTest () {

        assertEquals("The name should be \"John Williams\"", "John Williams", account0.getName());

    }

    @Test
    public void interestTest () {

        assertEquals("The interest rate should be 3%.", 0.03, account0.getInterestRate(), 0);

    }

    @Test
    public void statusTest () {

        assertEquals("The status should be open.", AccountStatus.OPEN, account0.getAccountStatus());

    }

    @Test
    public void overdraftTest () {

        assertEquals("The protection status should be disabled.", OverdraftPrevention.DISABLED, account0.getOverdraftPrevention());

    }

    @Test
    public void withdrawalRecordTest () {


        account0.credit(100);
        account0.debit(25);
        account0.debit(17);
        account0.debit(40);
        account0.debit(18);

        expectedList = "Your recent withdrawals:\n" +
                "1234: -$18.0\n" +
                "1234: -$40.0\n" +
                "1234: -$17.0\n" +
                "1234: -$25.0\n";

        assertEquals("The withdrawal history should be 18, 40, 17, 25", expectedList, account0.getWithdrawalHistory());


    }

    @Test
    public void depositRecordTest () {

        account0.credit(25);
        account0.credit(17);
        account0.credit(40);
        account0.credit(18);

        expectedList = "Your recent deposits:\n" +
                "1234: +$18.0\n" +
                "1234: +$40.0\n" +
                "1234: +$17.0\n" +
                "1234: +$25.0\n";

        assertEquals("The deposit history should be 18, 40, 17, 25", expectedList, account0.getDepositHistory());

    }

    @Test
    public void transferRecordTest () {

        account2.setAccountStatus(AccountStatus.FREEZE);
        account0.credit(100);
        account0.transfer(account1, 20);
        account0.transfer(account2, 40);
        account0.transfer(account1, 40);
        account0.transfer(account1, 50);


        expectedList = "Your recent transfers:\n" +
                "1234 ---> 1235: $40.0\n" +
                "1234 ---> 1235: $20.0\n";

        assertEquals("The transfer history should be 40, 20", expectedList, account0.getTransferHistory());


    }

    @Test
    public void statusRecordTest () {

        account0.setAccountStatus(AccountStatus.FREEZE);
        account0.setAccountStatus(AccountStatus.OPEN);
        account0.setAccountStatus(AccountStatus.CLOSED);
        account0.setAccountStatus(AccountStatus.OPEN);

        //Constructor not called here so no initial status in log
        expectedList = "Your recent status updates:\n1234 status: CLOSED\n1234 status: OPEN\n1234 status: FREEZE\n";

        assertEquals("The status history should be CLOSED, OPEN, FREEZE", expectedList, account0.getStatusHistory());

    }

    @Test
    public void nameRecordTest () {

        account0.setName("John Billiams");
        account0.setName("John Shilliams");
        account0.setName("Bill Johnson");
        account0.setName("Will Sonjohn");

        //Constructor not called here so no initial status in log
        expectedList = "Your recent name changes:\n1234 name: Will Sonjohn\n1234 name: Bill Johnson\n1234 name: John Shilliams\n1234 name: John Billiams\n";

        assertEquals("The name changes should be \"Will Sonjohn\", \"Bill Johnson\", \"John Shilliams\" then \"John Billiams\".", expectedList, account0.getNameHistory());
    }

    @Test
    public void interestRecordTest () {

        account0.setInterestRate(0.052);
        account0.setInterestRate(0.023);
        account0.setInterestRate(0.067);
        account0.setInterestRate(0.058);

        //Constructor not called here so no initial status in log
        expectedList = "Your recent interest rate changes:\n1234 interest: 6%\n1234 interest: 7%\n1234 interest: 2%\n1234 interest: 5%\n";

        assertEquals("The interest rate changed should be 6%, 7%, 2%, 5%.", expectedList, account0.getInterestHistory());

    }

    @Test
    public void checkBalanceOnFrozenAccountTest () {

        account2.setAccountStatus(AccountStatus.FREEZE);
        assertEquals("The value should be inaccessible to frozen accounts.", null, account2.getBalance());

    }


    @Test
    public void debitApprovalTest () {

        account0.credit(50);
        account0.debit(20);
        assertEquals("The value should be $30", 30, account0.getBalance(), 0);

    }


    @Test
    public void debitDenialClosedTest () {

        account3.setAccountStatus(AccountStatus.CLOSED);
        account3.debit(30);
        assertEquals("The value should be $0", 0, account3.getBalance(), 0);

    }

    @Test
    public void debitOverdraftApprovalTest () {

        account0.debit(40);
        assertEquals("The value should be $-10", -40, account0.getBalance(), 0);

    }

    @Test
    public void debitOverdraftDenialTest () {

        account0.credit(30);
        account0.setOverdraftPrevention(OverdraftPrevention.ENABLED);
        account0.debit(40);
        assertEquals("The value should be $30", 30, account0.getBalance(), 0);


    }

    @Test
    public void creditApprovalTest () {

        account0.credit(30);
        assertEquals("The value should be $30", 30, account0.getBalance(), 0);

    }

    @Test
    public void creditDenialClosedTest () {

        account3.setAccountStatus(AccountStatus.CLOSED);
        account3.credit(50);
        assertEquals("Closed accounts should have a balance of $0", 0, account3.getBalance(), 0);

    }

    @Test
    public void transferApprovalTest () {

        account0.credit(50);
        account0.transfer(account1, 40);
        assertEquals("The checking account should receive $40", 40, account1.getBalance(), 0);

    }

    @Test
    public void transferDenialTest () {

        account1.credit(40);
        account0.transfer(account1, 20);
        assertEquals("Cannot overdraft on transfer", 40, account1.getBalance(), 0);

    }

    @Test
    public void changeNameTest () {

        account0.setName("John Billiams");
        assertEquals("Name should be changed to \"John Billiams\".", "John Billiams", account0.getName());

    }


    @Test
    public void changeNameDeniedTest () {

        account3.setAccountStatus(AccountStatus.CLOSED);
        account3.setName("John Crilliams");
        assertEquals("Cannot change name of a closed account.", "John Williams", account3.getName());

    }

    @Test
    public void reopeningAClosedAccountTest () {

        account3.setAccountStatus(AccountStatus.CLOSED);
        account3.setAccountStatus(AccountStatus.OPEN);
        assertEquals("Cannot reopen a closed account.", AccountStatus.CLOSED, account3.getAccountStatus());
    }

    @Test
    public void closingAccountWithBalanceTest () {
        account0.credit(50);
        account0.setAccountStatus(AccountStatus.CLOSED);
        assertEquals("Cannot close an account with a balance.", AccountStatus.OPEN, account0.getAccountStatus());

    }

    @Test
    public void overdraftOnAccountWithAutoTransferPass () {
        account0.setOverdraftPrevention(account1);
        account0.credit(20);
        account1.credit(50);
        account0.debit(30);

        assertEquals("The checking account should have $40", 40, account1.getBalance(), 0);
    }

    @Test
    public void doubleOverdraftOnAccountWithAutoTransferFail () {
        account0.setOverdraftPrevention(account1);
        account1.setOverdraftPrevention(account3);
        account0.credit(20);
        account1.credit(5);
        account3.credit(15);
        account0.debit(30);

        assertEquals("The checking account should have $10", 10, account3.getBalance(), 0);
    }

    @Test
    public void overdraftOnAccountWithAutoTransferFail () {
        account0.setOverdraftPrevention(account1);
        account0.credit(20);
        account1.credit(5);
        account0.debit(30);


        assertEquals("The checking account should have $5", 5, account1.getBalance(), 0);
    }



}
