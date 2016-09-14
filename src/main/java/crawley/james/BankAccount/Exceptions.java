package crawley.james.BankAccount;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class Exceptions {


    boolean canDebitAccount (Account account, double amount, boolean isTransfer) {

        if ( isOpen(account.getAccountStatus()) && ( (!isOverdraftPrevented(account) && !isTransfer) || amount <= account.getBalance())) {

            return true;

        } else {

            return false;
        }
    }

    boolean canCreditAccount (Account account) {

        if (isOpen(account.getAccountStatus())) {

            return true;

        } else {

            return false;
        }
    }

    boolean canChangeAccountStatus (Account account, AccountStatus status) {

        if (account.getAccountStatus().equals(AccountStatus.CLOSED)
                || (status.equals(AccountStatus.CLOSED) && account.getBalance() > 0)) {

            return false;

        } else {

            return true;

        }

    }

    boolean canChangeName (AccountStatus status) {

        if (!isClosed(status)) {

            return true;

        } else {

            return false;
        }

    }

    boolean canCheckBalance(AccountStatus status) {

        if (status.equals(AccountStatus.FREEZE)) {

            return false;

        } else {

            return true;

        }
    }

    private boolean isOpen (AccountStatus  status) {

        return status.equals(AccountStatus.OPEN);

    }

    private boolean isOverdraftPrevented (Account account) {

        return account.getOverdraftPrevention().equals(OverdraftPrevention.ENABLED);
    }

    private boolean isClosed (AccountStatus status) {

        return status.equals(AccountStatus.CLOSED);
    }

}
