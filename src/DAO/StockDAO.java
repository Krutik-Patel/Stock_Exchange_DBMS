package DAO;

import Table.*;
import Join_Table.*;
import java.util.Date;
import java.util.ArrayList;

public interface StockDAO {
	public Stock getStockByKey(int stock_id);

	public void addStock(Stock stock) throws Exception;

	public void updateStock(Stock stock) throws Exception;

	public void deleteStock(Stock stock);

	public Stock_Analysis get_stock_analysis(int stock_id, String date) throws Exception;

	public ArrayList<Market_Trend> get_market_trend(String start_date, String end_date) throws Exception;
}
