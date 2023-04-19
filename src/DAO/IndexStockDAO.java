package DAO;
import Table.*;

public interface IndexStockDAO {
    public IndexStock getIndexStockByKey(int index_stk_id);
	public void addIndexStock(IndexStock indexStock) throws Exception;
	public void updateIndexStock(IndexStock indexStock) throws Exception;
	public void deleteIndexStock(IndexStock indexStock);
}
