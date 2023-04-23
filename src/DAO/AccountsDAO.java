package DAO;

import Table.*;

public interface AccountsDAO {
	public Accounts getAccountByKey(int account_id);

	public void addAccount(Accounts account) throws Exception;

	public void updateAccount(Accounts account) throws Exception;

	public void deleteAccount(Accounts account);

	public int getNextID();
}
