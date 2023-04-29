package DAO;
import Table.*;
import Join_Table.*;
import java.util.ArrayList;

public interface IndexStockDAO {
    public IndexStock getIndexStockByKey(int index_stk_id) throws Exception;
	public void addIndexStock(IndexStock indexStock) throws Exception;
	public void updateIndexStock(IndexStock indexStock) throws Exception;
	public void deleteIndexStock(IndexStock indexStock) throws Exception;
	public void recalculate() throws Exception;
	public ArrayList<Index_Info> getIndexStockInfo() throws Exception;
}
