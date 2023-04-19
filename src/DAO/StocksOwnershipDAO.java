package DAO;

import Table.*;

public interface StocksOwnershipDAO {
	public Stock getStocksOnwershipByKey(int stock_id, int owner_id);

	public void addStocksOwnership(StocksOwnership stocksOwnership) throws Exception;

	public void updateStock(StocksOwnership stocksOwnership) throws Exception;

	public void deleteStock(StocksOwnership stocksOwnership);
}
