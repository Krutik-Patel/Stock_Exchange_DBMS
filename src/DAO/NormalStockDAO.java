package DAO;
import Tables.*;

public interface NormalStockDAO {
    public NormalStock getNormalStockByKey(int normal_stk_id);
	public void addNormalStock(NormalStock normalStock) throws Exception;
	public void updateNormalStock(NormalStock normalStock) throws Exception;
	public void deleteNormalStock(NormalStock normalStock);
}
