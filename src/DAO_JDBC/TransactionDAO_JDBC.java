package DAO_JDBC;

import DAO.TransactionDAO;
import Table.*;

import java.util.Date;
import java.sql.*;

import java.text.SimpleDateFormat;

public class TransactionDAO_JDBC implements TransactionDAO {
    Connection dbConnection;

	public TransactionDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
	public Transaction getTransactionByKey(int trans_id) {
		Transaction t = new Transaction();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select trans_id,acc_id_from,acc_id_to,stk_id,units,trans_date,trans_price from Transaction where trans_id = " + trans_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int id = rs.getInt("trans_id");
                int acc_id_from = rs.getInt("acc_id_from");
                int acc_id_to = rs.getInt("acc_id_to");
                int stk_id = rs.getInt("stk_id");
                int units = rs.getInt("units");
                Date trans_date = rs.getDate("trans_date");
                float trans_price = rs.getFloat("trans_price");
				
                t.set_trans_id(id);
                t.set_acc_id_from(acc_id_from);
                t.set_acc_id_to(acc_id_to);
                t.set_stk_id(stk_id);
                t.set_units(units);
                t.set_trans_date(trans_date);
                t.set_trans_price(trans_price);
				
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

		return t;
	}

	@Override
	public void addTransaction(Transaction Transaction) throws Exception {
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into Transaction(trans_id,acc_id_from,acc_id_to,stk_id,units,trans_date,trans_price) values (?,?,?,?,?,?,?)";

		String d = Transaction.get_trans_date();
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(d);

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, Transaction.get_trans_id());
            preparedStatement.setInt(2, Transaction.get_acc_id_from());
            preparedStatement.setInt(3, Transaction.get_acc_id_to());
            preparedStatement.setInt(4, Transaction.get_stk_id());
            preparedStatement.setInt(5, Transaction.get_units());
			preparedStatement.setDate(6, new java.sql.Date(date.getTime()));
			preparedStatement.setFloat(7, Transaction.get_trans_price());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Transaction with ID " + Transaction.get_trans_id()
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
	public void updateTransaction(Transaction Transaction) throws Exception {

		PreparedStatement preparedStatement = null;
		String sql;

		sql = "update Transaction set acc_id_from=?,acc_id_to=?,stk_id=?,units=?,trans_date=?,trans_price=? where trans_id = "+ Transaction.get_trans_id();

		
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			String d = Transaction.get_trans_date();
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(d);

            preparedStatement.setInt(1, Transaction.get_acc_id_from());
            preparedStatement.setInt(2, Transaction.get_acc_id_to());
            preparedStatement.setInt(3, Transaction.get_stk_id());
            preparedStatement.setInt(4, Transaction.get_units());
			preparedStatement.setDate(5, new java.sql.Date(date.getTime()));
			preparedStatement.setFloat(6, Transaction.get_trans_price());
			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("Transaction with ID: " + Transaction.get_trans_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for ID: " + Transaction.get_trans_id());
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
	public void deleteTransaction(Transaction Transaction) {
		// Deletion
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM Transaction where trans_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, Transaction.get_trans_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("Transaction with ID " + Transaction.get_trans_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for ID: " + Transaction.get_trans_id());
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
