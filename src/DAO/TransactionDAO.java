package DAO;
import Table.*;
import Join_Table.Transaction_History;

import java.util.ArrayList;

public interface TransactionDAO {
    public Transaction getTransactionByKey(int trans_id);
	public void addTransaction(Transaction transaction) throws Exception;
	public void updateTransaction(Transaction transaction) throws Exception;
	public void deleteTransaction(Transaction transaction);
	
	public ArrayList<Transaction_History> getTransactionHistory(int account_id);
}
