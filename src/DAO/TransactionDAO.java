package DAO;
import Tables.*;

public interface TransactionDAO {
    public Transaction getTransactionByKey(int trans_id);
	public void addTransaction(Transaction transaction) throws Exception;
	public void updateTransaction(Transaction transaction) throws Exception;
	public void deleteTransaction(Transaction transaction);
}
