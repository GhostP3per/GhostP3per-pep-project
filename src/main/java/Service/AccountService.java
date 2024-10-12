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
     * Use the accountDAO to register an account to the database.
     * 
     * @param account an account object.
     * 
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

    /**
     * Use the accountDAO to verify login info with existing account from database.
     * 
     * @param account an account object.
     * 
     * @return account if username and password provided in request body
     * JSON match a real acocunt existing on the database
     */
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

    /**
     * Use the accountDAO to get an account object from database.
     * 
     * @param account_id - an integer representing the account_id of the account that we want
     * to retrieve from the database.
     * 
     * @return accfromDatabase - account object that matched the account_id from the database
     */
    public Account getAccountById(int account_id) {
        Account accfromDatabase = accountDAO.getAccountById(account_id);
        return accfromDatabase;
    }
}
