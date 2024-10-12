package Service;


import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
    
    /**
     * TODO: Use the accountDAO to register an account to the database.
     * @param account an account object.
     * @return account if it was successfully persisted, null if any of these codition fails: 
     * username not blank
     * password is at least 4 characters long
     * and account with that username doesn't already exist
     */
    public Account insertAccount(Account account) {
        if (
            (account.getUsername().length() > 0)
            && (account.getPassword().length() >= 4)
            && (accountDAO.getAccountByUsername(account.getUsername()) == null) 
            ) {
            return accountDAO.insertAccount(account);
        }
        return null;
    }

    public Account loginAccount(Account account) { 
        if (account.getUsername().length() > 0) {
            Account accfromDatabase = accountDAO.getAccountByUsername(account.getUsername());
            if (accfromDatabase != null) {
                if ((account.getUsername().equals(accfromDatabase.getUsername())) && (account.getPassword().equals(accfromDatabase.getPassword()))) {
                    return accfromDatabase;
                } 
            }        
        }
        return null;
    }
    public Account getAccountById(int account_id) {
        Account accfromDatabase = accountDAO.getAccountById(account_id);
        return accfromDatabase;
    }
}
