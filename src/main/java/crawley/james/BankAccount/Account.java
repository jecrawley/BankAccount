package crawley.james.BankAccount;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class Account {

    private final AccountType type;
    private final String id;
    private String name;
    private double interest = 0.03;
    private AccountStatus status = AccountStatus.OPEN;
    private double balance = 0;
    private OverdraftPrevention prevention = OverdraftPrevention.DISABLED;
    private Exceptions exceptions = new Exceptions();
    private Transactions transactions = new Transactions();
    private ArrayList<String> usedIds = new ArrayList<String>();


    public Account (AccountType type, String name) {
        String tempId;

        this.type = type;
        while (true) {
            tempId = generateId(11);
            if (isUnusedId(tempId)) {
                id = tempId;
                usedIds.add(id);
                break;
            }
        }

        transactions.updateStatusHistory(status, id);
        transactions.updateNameHistory(name, id);
        transactions.updateInterestHistory(interest, id);

        this.name = name;

    }

    //Overloading for the purposed of testing transaction history without random IDs
    public Account (AccountType type, String name, String id) {
        this.id = id;
        this.type = type;
        this.name = name;

    }


    boolean isUnusedId (String id) {
        boolean unusedId = false;

        if (!(usedIds.contains(id))) {
            unusedId = true;
        }
        return unusedId;
    }

    String getId () {

        return id;
    }

    Double getBalance () {

        if (exceptions.canCheckBalance(this.getAccountStatus())) {

            return balance;

        } else {

            return null;

        }

    }

    private void setBalance (double amount) {

        balance = amount;

    }

    void setName (String name) {

        if (exceptions.canChangeName(this.getAccountStatus())) {

            transactions.updateNameHistory(name, id);
            this.name = name;

        }
    }

    String getName () {

        return name;
    }

    boolean debit (double amount) {

        if (exceptions.canDebitAccount(this, amount, false)) {

            setBalance(balance - amount);
            transactions.updateWithdrawals(amount, this.getId());
            return true;

        } else {

            return false;

        }

    }

    boolean credit (double amount) {

        if (exceptions.canCreditAccount(this)) {

            setBalance(balance + amount);
            transactions.updateDeposits(amount, this.id);
            return true;

        } else {

            return false;

        }

    }

    boolean transfer (Account receivingAccount, double amount) {

        if (sameOwner(receivingAccount) && exceptions.canCreditAccount(receivingAccount) &&
                exceptions.canDebitAccount(this, amount, true)) {

            receivingAccount.credit(amount);
            this.debit(amount);
            transactions.updateTransfers(amount, receivingAccount.getId(), this.getId());
            return true;

        } else {

            return false;

        }
    }

    void setInterestRate (double interest) {

        transactions.updateInterestHistory(interest, id);
        this.interest = interest;
    }

    double getInterestRate () {
        return interest;
    }

    void setAccountStatus (AccountStatus status) {

        if (exceptions.canChangeAccountStatus(this, status)) {

            transactions.updateStatusHistory(status, id);
            this.status = status;

        }
    }

    AccountStatus getAccountStatus () {

        return status;
    }

    void setOverdraftPrevention (OverdraftPrevention prevention) {

        this.prevention = prevention;
    }

    OverdraftPrevention getOverdraftPrevention () {

        return prevention;
    }

    ArrayList<String> getTransactions (TransactionType transaction) {
        return null;
    }

    private String generateId (int length) {
        Random random = new Random();
        String randomId = "";
        for (int i = 0; i < length; i++) {
            randomId += random.nextInt(10);
        }
        return randomId;
    }

    private boolean sameOwner (Account receivingAccount) {
        if (this.name.equals(receivingAccount.getName())) {

            return true;

        } else {

            return false;
        }
    }

    String getWithdrawalHistory () {

        return transactions.requestWithdrawalHistory();
    }

    String getDepositHistory () {

        return transactions.requestDepositHistory();
    }

    String getTransferHistory () {

        return transactions.requestTransferHistory();
    }

    String getStatusHistory () {

        return transactions.requestStatusHistory();
    }

    String getNameHistory () {

        return transactions.requestNameHistory();
    }

    String getInterestHistory () {

        return transactions.requestInterestHistory();

    }
}
