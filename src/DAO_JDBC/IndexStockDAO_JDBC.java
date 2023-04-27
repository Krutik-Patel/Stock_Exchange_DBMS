package DAO_JDBC;
import DAO.*;
import Table.*;
import Join_Table.*;
import java.util.ArrayList;
import java.sql.*;

public class IndexStockDAO_JDBC implements IndexStockDAO {
    Connection dbConnection;

    public IndexStockDAO_JDBC(Connection dbconn) {
        dbConnection = dbconn;
    }

    @Override
    public IndexStock getIndexStockByKey(int index_stk_id) throws Exception {
        IndexStock stk = new IndexStock();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select index_stk_id,stock_quant from IndexStock where index_stk_id = " + index_stk_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int index_stock_id = rs.getInt("index_stk_id");
                int stock_quant = rs.getInt("stock_quant");
                
				
                stk.set_index_stk_id(index_stock_id);
                stk.set_stock_quant(stock_quant);
				
				break;
				// Add exception handling here if more than one row is returned
			}
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		// Add exception handling when there is no matching record
		if (!flag) {
			System.out.println("No Record Found");
			return null;
		}

		return stk;
    }

    @Override
    public void addIndexStock(IndexStock indexStock) throws Exception {
        PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into IndexStock(index_stk_id, stock_quant) values (?,?)";

		

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, indexStock.get_index_stk_id());
			preparedStatement.setInt(2, indexStock.get_stock_quant());
            

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("IndexStock with ID " + indexStock.get_index_stk_id()
					+ ", added to the database");
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

    @Override
    public void updateIndexStock(IndexStock indexStock) throws Exception{
        PreparedStatement preparedStatement = null;
		String sql;

		sql = "update IndexStock set stock_quant =? where index_stk_id = "+ indexStock.get_index_stk_id();

		
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			
			preparedStatement.setInt(1, indexStock.get_stock_quant());


			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("IndexStock with ID: " + indexStock.get_index_stk_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for ID: " + indexStock.get_index_stk_id());
			}

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
    
    @Override
    public void deleteIndexStock(IndexStock indexStock) throws Exception{
        PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM IndexStock where index_stk_id = ?";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, indexStock.get_index_stk_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("IndexStock with ID " + indexStock.get_index_stk_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for ID: " + indexStock.get_index_stk_id());
			}

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

    @Override
    public void recalculate() throws Exception{
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

    @Override
    public ArrayList<Index_Info> getIndexStockInfo() throws Exception{
        ArrayList<Index_Info> index_info_list = new ArrayList<>();
		String sql = "select index_stk_id, stock_name, stock_units, stock_price, dom_id, domain_name, stock_quant from Stock inner join IndexStock on stock_id = index_stk_id inner join Domain on dom_id = domain_id";
		
		Statement stmt = null;

		try {
			stmt = dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				Index_Info index_Info = new Index_Info();
		
                int index_stk_id = rs.getInt("index_stk_id");
                String stock_name = rs.getString("stock_name");
                int stock_units = rs.getInt("stock_units");
                float stock_price = rs.getFloat("stock_price");
                int dom_id = rs.getInt("dom_id");
                String domain_name = rs.getString("domain_name");
                int stock_quant = rs.getInt("stock_quant");

				index_Info.set_index_stk_id(index_stk_id);
                index_Info.set_stock_name(stock_name);
                index_Info.set_stock_units(stock_units);
                index_Info.set_stock_price(stock_price);
                index_Info.set_dom_id(dom_id);
                index_Info.set_domain_name(domain_name);
                index_Info.set_stock_quant(stock_quant);

				index_info_list.add(index_Info);
			}
		
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return index_info_list;
    }
}
