package crawley.james.BankAccount;

/**
 * Created by jamescrawley on 9/13/16.
 */
public class Exceptions {


    boolean canDebitAccount (Account account, double amount, boolean isTransfer) {

        if ( (isNotOverdraftPrevented(account) && !isTransfer) || amount <= account.getBalance()) {

            return true;

        } else  if (hasOverdraftAutoTransfer(account)){

            return account.getAutoTransferAccount().transfer(account, amount - account.getBalance());

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

        if (isClosed(account.getAccountStatus()) || (status.equals(AccountStatus.CLOSED) && account.getBalance() != 0)) {

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

    private boolean isNotOverdraftPrevented (Account account) {

        return account.getOverdraftPrevention().equals(OverdraftPrevention.DISABLED);
    }

    private boolean isClosed (AccountStatus status) {

        return status.equals(AccountStatus.CLOSED);
    }

    private boolean hasOverdraftAutoTransfer (Account account) {

        return account.getOverdraftPrevention().equals(OverdraftPrevention.TRANSFER);

    }

}
