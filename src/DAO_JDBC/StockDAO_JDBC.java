package DAO_JDBC;

import DAO.StockDAO;
import Table.*;
import Join_Table.*;
import java.sql.*;
import java.util.ArrayList;

public class StockDAO_JDBC implements StockDAO {

	Connection dbConnection;

	public StockDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
	public Stock getStockByKey(int stock_id) {
		Stock s = new Stock();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select stock_id,stock_name,stock_units,stock_price,dom_id from Stock where stock_id = " + stock_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int stk_id = rs.getInt("stock_id");
				String stock_name = rs.getString("stock_name");
				int stock_units = rs.getInt("stock_units");
				float stock_price = rs.getFloat("stock_price");
				int dom_id = rs.getInt("dom_id");
				s.set_stock_id(stk_id);
				s.set_stock_name(stock_name);
				s.set_stock_units(stock_units);
				s.set_stock_price(stock_price);
				s.set_dom_id(dom_id);

				break;
				// Add exception handling here if more than one row is returned
			}
			if (rs.next()) {
				System.out.println("More than one record found for the given regs_id");
				return null;
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

		return s;
	}

	@Override
	public void addStock(Stock stock) throws Exception {
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into Stock(stock_id,stock_name,stock_units,stock_price,dom_id) values (?,?,?,?,?)";

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, stock.get_stock_id());
			preparedStatement.setString(2, stock.get_stock_name());
			preparedStatement.setInt(3, stock.get_stock_units());
			preparedStatement.setFloat(4, stock.get_stock_price());
			preparedStatement.setInt(5, stock.get_dom_id());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Stock with ID " + stock.get_stock_id()
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
	public void updateStock(Stock stock) throws Exception {

		PreparedStatement preparedStatement = null;
		String sql;
		sql = "update Stock set stock_name=?,stock_units=?,stock_price=?,dom_id=? where stock_id = "
				+ stock.get_stock_id();
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setString(1, stock.get_stock_name());
			preparedStatement.setInt(2, stock.get_stock_units());
			preparedStatement.setFloat(3, stock.get_stock_price());
			preparedStatement.setInt(4, stock.get_dom_id());

			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("Stock with stock_id: " + stock.get_stock_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for regs_id: " + stock.get_stock_id());
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
	public void deleteStock(Stock stock) {
		// Deletion
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM Stock where stock_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, stock.get_stock_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("Stock with id " + stock.get_stock_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for id: " + stock.get_stock_id());
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
	public Stock_Analysis get_stock_analysis(int stock_id,String date) throws Exception {
        Stock_Analysis s = new Stock_Analysis();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select count(trans_id) as total_transaction,sum(trans_price) as total_trans_price , sum(units) as total_units ,stock_id   from Stock , Transaction where stock_id = stk_id and trans_date > "+ date +" and stock_id="+stock_id+ " group by stock_id";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				float total_trans_price = rs.getFloat("total_trans_price");
				int total_transaction = rs.getInt("total_transaction");
                int total_units = rs.getInt("total_units");
                int stk_id = rs.getInt("stock_id");
              
				
                s.set_total_trans_price(total_trans_price);
				s.set_total_transaction(total_transaction);
                s.set_total_units(total_units);
                s.set_stock(getStockByKey(stk_id));
				
				// Add exception handling here if more than one row is returned
			}
			if (rs.next()) {
				System.out.println("More than one record found for the given regs_id");
				return null;
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

		return s;
	}
	
	public ArrayList<Market_Trend> get_market_trend(String start_date, String end_date) throws Exception {
		ArrayList<Market_Trend> markTrendList = new ArrayList<>();
		String sql = "with stkvw as (select st.stock_id, IFNULL(tb.trans_price/tb.units, st.stock_price) as past_price, st.stock_price curr_price, st.stock_name, ROW_NUMBER() over(partition by st.stock_id order by trans_date) as row_no from Stock st left join (select * from Transaction t where trans_date between \"" + start_date +"\" and \"" + end_date + "\" order by trans_date) tb on st.stock_id = tb.stk_id) select stock_name, stock_id, past_price as prev_price, curr_price from stkvw where row_no = 1";
		
		Statement stmt = null;

		try {
			stmt = dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				Market_Trend market_Trend = new Market_Trend();
				String stock_name = rs.getString("stock_name");
				int stock_id = rs.getInt("stock_id");
				float prev_price = rs.getFloat("prev_price");
				float curr_price = rs.getFloat("curr_price");


				market_Trend.set_curr_price(curr_price);
				market_Trend.set_stock_id(stock_id);
				market_Trend.set_stock_name(stock_name);
				market_Trend.set_prev_price(prev_price);
				markTrendList.add(market_Trend);
			}
		
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return markTrendList;
	}
}
