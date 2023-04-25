package DAO_JDBC;
import DAO.*;
import Table.*;
import java.sql.*;

public class IndexStockDAO_JDBC implements IndexStockDAO {
    Connection dbConnection;

    public IndexStockDAO_JDBC(Connection dbconn) {
        dbConnection = dbconn;
    }

    public IndexStock getIndexStockByKey(int index_stk_id) {
        IndexStock indStk = new IndexStock();
        return indStk;
    }

    public void addIndexStock(IndexStock indexStock) {

    }

    public void updateIndexStock(IndexStock indexStock) {

    }
    
    public void deleteIndexStock(IndexStock indexStock) {

    }

    public void recalculate() {
        PreparedStatement preparedStatement = null;
		String sql;
		sql = "with finvw as (with xtvw as (with vw as (select normal_stk_id, stk_comp_id, dom_id, stock_price, ROW_NUMBER() over(partition by dom_id order by stock_price desc) as row_no from NormalStock, Stock where normal_stk_id = stock_id) select dom_id, avg(stock_price) as avg from vw where row_no between 1 and 3 group by dom_id) select l.dom_id, avg, stock_id from xtvw, (select stock_id, dom_id from Stock, IndexStock where stock_id = index_stk_id) l where l.dom_id = xtvw.dom_id) update Stock stk, finvw set stock_price = finvw.avg where finvw.stock_id = stk.stock_id;";

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			// execute insert SQL stetement
			int ret = preparedStatement.executeUpdate();
            System.out.println("Updated All Index Stocks.");
		
        } catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
}
