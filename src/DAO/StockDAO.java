package DAO;

import Table.*;

public interface StockDAO {
	public Stock getStockByKey(int stock_id);

	public void addStock(Stock stock) throws Exception;

	public void updateStock(Stock stock) throws Exception;

	public void deleteStock(Stock stock);
}
