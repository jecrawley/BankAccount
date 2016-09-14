package crawley.james.BankAccount;

import java.util.ArrayList;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class Transactions {

    ArrayList<String> withdrawals = new ArrayList<String>();
    ArrayList<String> deposits = new ArrayList<String>();
    ArrayList<String> transfers = new ArrayList<String>();
    ArrayList<String> statusHistory = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> interest = new ArrayList<String>();

    void updateWithdrawals (double amount, String accountId) {

        //Changes the color of the money to red for withdrawals, from StackOverflow
        String transaction = accountId + ":" + /*"\u001B[31m" +*/  " -$" + amount /*+ "\u001B[0m"*/;
        withdrawals.add(0, transaction);

    }

    String requestWithdrawalHistory () {

        String withdrawalHistory = "Your recent withdrawals:\n";

        for (String transaction : withdrawals) {
            withdrawalHistory += transaction + "\n";
        }

        return withdrawalHistory;
    }

    void updateDeposits (double amount, String accountId) {

        //Changes the color of the money to green for deposits, from StackOverflow
        String transaction = accountId + ":" + /*"\u001B[32m" +*/  " +$" + amount /*+ "\u001B[0m"*/;
        deposits.add(0, transaction);

    }

    String requestDepositHistory () {

        String depositHistory = "Your recent deposits:\n";

        for (String transaction : deposits) {
            depositHistory += transaction + "\n";
        }

        return depositHistory;
    }


    void updateTransfers (double amount, String creditId, String debitId) {

        String transaction = debitId + " ---> " + creditId + ":" + /*"\u001B[32m" +*/  " $" + amount  /*+ "\u001B[0m"*/;
        transfers.add(0, transaction);

    }

    String requestTransferHistory() {

        String transferHistory = "Your recent transfers:\n";

        for (String transaction : transfers) {
            transferHistory += transaction + "\n";
        }

        return transferHistory;
    }

    void updateStatusHistory (AccountStatus status, String accountId) {

        String transaction = accountId + " status: " + status;
        statusHistory.add(0, transaction);

    }

    String requestStatusHistory () {

        String statusHistory = "Your recent status updates:\n";

        for (String transaction : this.statusHistory) {
            statusHistory += transaction + "\n";
        }

        return statusHistory;
    }

    void updateNameHistory (String name, String accountId) {

        String transaction = accountId + " name: " + name;
        this.name.add(0, transaction);

    }

    String requestNameHistory () {

        String nameHistory = "Your recent name changes:\n";

        for (String transaction : name) {
            nameHistory += transaction + "\n";
        }

        return nameHistory;
    }

    void updateInterestHistory (double interest, String accountId) {

        String transaction = accountId + " interest: " + (int)Math.round(interest * 100) + "%";
        this.interest.add(0, transaction);

    }

    String requestInterestHistory () {

        String interestHistory = "Your recent interest rate changes:\n";

        for (String transaction : interest) {
            interestHistory += transaction + "\n";
        }

        return interestHistory;

    }

}
