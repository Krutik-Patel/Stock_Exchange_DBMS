package DAO_JDBC;

import DAO.AccountsDAO;


import Table.*;
import java.sql.*;


public class AccountsDAO_JDBC implements  AccountsDAO {
    
	Connection dbConnection;

	public AccountsDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
	public Accounts getAccountByKey(int acc_id) {
		Accounts c = new Accounts();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select account_id,holder_regs_id,password,bank_acc_no from Accounts where account_id = " + acc_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int account_id = rs.getInt("account_id");
                int holder_regs_id = rs.getInt("holder_regs_id");
                String password = rs.getString("password");
                String bank_acc_no = rs.getString("bank_acc_no");
				
                c.set_account_id(account_id);
                c.set_holder_regs_id(holder_regs_id);
                c.set_password(password);
                c.set_bank_acc_no(bank_acc_no);
				
				break;
				// Add exception handling here if more than one row is returned
			}
			if (rs.next()) {
				System.out.println("More than one record found for the given comp_id");
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

		return c;
	}

	@Override
	public void addAccount(Accounts accounts) throws Exception {
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into Accounts (account_id,holder_regs_id,password,bank_acc_no) values (?,?,?,?)";

		

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, accounts.get_account_id());
			preparedStatement.setInt(2, accounts.get_holder_regs_id());
            preparedStatement.setString(3, accounts.get_password());
            preparedStatement.setString(4, accounts.get_bank_acc_no());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Accounts with ID " + accounts.get_account_id()
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
	public void updateAccount(Accounts accounts) throws Exception {

		PreparedStatement preparedStatement = null;
		String sql;

		sql = "update Accounts set holder_regs_id=?,password=?,bank_acc_no=? where account_id = "+ accounts.get_account_id();

		
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			
			preparedStatement.setInt(1, accounts.get_holder_regs_id());
            preparedStatement.setString(2, accounts.get_password());
            preparedStatement.setString(3, accounts.get_bank_acc_no());


			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("Accounts with ID: " + accounts.get_account_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for ID: " + accounts.get_account_id());
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
	public void deleteAccount(Accounts accounts) {
		// Deletion
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM Accounts where account_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, accounts.get_account_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("Accounts with ID " + accounts.get_account_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for ID: " + accounts.get_account_id());
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

    public int getNextID() {
		Statement preparedStatement = null;
		String sql;
		sql = "select max(account_id) from Accounts";
		try {
			preparedStatement = dbConnection.createStatement();

			// execute delete SQL stetement
			ResultSet rs = preparedStatement.executeQuery(sql);

			rs.next();
			int totalAcc = rs.getInt("max(account_id)");
			return totalAcc;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
}

