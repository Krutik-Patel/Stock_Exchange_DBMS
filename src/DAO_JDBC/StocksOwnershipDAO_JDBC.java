package DAO_JDBC;

import DAO.StocksOwnershipDAO;
import Table.*;
import java.sql.*;

public class StocksOwnershipDAO_JDBC implements StocksOwnershipDAO {

	Connection dbConnection;

	public StocksOwnershipDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
    public StocksOwnership getStocksOnwershipByKey(int stockID, int ownerID) throws Exception{
        StocksOwnership stk = new StocksOwnership();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select * from StocksOwnership where stck_id = " + stockID + " and owner_id = " + ownerID;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
                int stck_ID = rs.getInt("stck_id");
                int owner_ID = rs.getInt("owner_id");
                int units_Owned = rs.getInt("units_owned");

                stk.set_owner_id(owner_ID);
                stk.set_stck_id(stck_ID);
                stk.set_units_owned(units_Owned);
                
				// Add exception handling here if more than one row is returned
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		if (!flag) {
			System.out.println("No Record Found");
			return null;
		}

		return stk;
    }

	@Override
	public void addStocksOwnership(StocksOwnership stocksOwnership) throws Exception{
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into StocksOwnership(stck_id, owner_id, units_owned) values (?,?,?)";

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, stocksOwnership.get_stck_id());
			preparedStatement.setInt(2, stocksOwnership.get_owner_id());
			preparedStatement.setInt(3, stocksOwnership.get_units_owned());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Added StocksOwnership : stockID = " + stocksOwnership.get_stck_id() + "accountID = " + stocksOwnership.get_owner_id());
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
	public void updateStockOwnership(StocksOwnership stocksOwnership) throws Exception{
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "update StocksOwnership set stck_id=?,owner_id=?,units_owned=? where stck_id = "
				+ stocksOwnership.get_stck_id() + "and owner_id = " + stocksOwnership.get_owner_id();
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, stocksOwnership.get_stck_id());
			preparedStatement.setInt(2, stocksOwnership.get_owner_id());
			preparedStatement.setInt(3, stocksOwnership.get_units_owned());

			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("Updated StocksOwnership : stockID = " + stocksOwnership.get_stck_id() + "accountID = " + stocksOwnership.get_owner_id());
			} else {
				System.out.println("Update Failed for owner_id: " + stocksOwnership.get_owner_id());
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
	public void deleteStockOwnership(StocksOwnership stocksOwnership) throws Exception{
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM StocksOwnership where stck_id = ? and owner_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, stocksOwnership.get_stck_id());
			preparedStatement.setInt(2, stocksOwnership.get_owner_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("Deleted StocksOwnership : stockID = " + stocksOwnership.get_stck_id() + "accountID = " + stocksOwnership.get_owner_id());
			} else {
				System.out.println("Delete Failed for id: " + stocksOwnership.get_stck_id());
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
	
}
