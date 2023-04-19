package DAO;

import Table.*;

public interface AccountsDAO {
	public Stock getAccountByKey(int account_id);

	public void addAccount(Accounts account) throws Exception;

	public void updateAccount(Accounts account) throws Exception;

	public void deleteAccount(Accounts account);
}
