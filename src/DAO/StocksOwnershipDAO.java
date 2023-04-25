package DAO;

import Table.*;

public interface StocksOwnershipDAO {
	public StocksOwnership getStocksOnwershipByKey(int stock_id, int owner_id)throws Exception;

	public void addStocksOwnership(StocksOwnership stocksOwnership) throws Exception;

	public void updateStockOwnership(StocksOwnership stocksOwnership) throws Exception;

	public void deleteStockOwnership(StocksOwnership stocksOwnership)throws Exception;
}
