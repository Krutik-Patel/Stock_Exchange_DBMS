package DAO;

import Table.*;
import Join_Table.*;

public interface StockDAO {
	public Stock getStockByKey(int stock_id);

	public void addStock(Stock stock) throws Exception;

	public void updateStock(Stock stock) throws Exception;

	public void deleteStock(Stock stock);

	public Stock_Analysis get_stock_analysis(int stock_id) throws Exception;
}
