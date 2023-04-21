package DAO_JDBC;

import DAO.UserDAO;
import Table.*;
import java.util.Date;
import java.sql.*;

import java.text.SimpleDateFormat;

public class UserDAO_JDBC implements UserDAO {

	Connection dbConnection;

	public UserDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
	public User getUserByKey(int user_id) {
		User u = new User();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select user_id,fname,mname,lname,dob from User where user_id = " + user_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int usr_id = rs.getInt("user_id");
                String fname = rs.getString("fname");
                String mname = rs.getString("mname");
                String lname = rs.getString("lname");
				Date dob = rs.getDate("dob");
				u.set_user_id(usr_id);
                u.set_fname(fname);
                u.set_mname(mname);
                u.set_lname(lname);
                u.set_dob(dob);
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

		return u;
	}

	@Override
	public void addUser(User User) throws Exception {
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into User(user_id,fname,mname,lname,dob) values (?,?,?,?,?)";

		String d = User.get_dob();
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(d);

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, User.get_user_id());
			preparedStatement.setString(2, User.get_fname());
			preparedStatement.setString(3, User.get_mname());
			preparedStatement.setString(4, User.get_lname());
			preparedStatement.setDate(5, new java.sql.Date(date.getTime()));

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("User with ID " + User.get_user_id()
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
	public void updateUser(User User) throws Exception {

		PreparedStatement preparedStatement = null;
		String sql;

		sql = "update User set fname=?,mname=?,lname=?,dob=? where user_id = "+ User.get_user_id();

		
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			String d = User.get_dob();
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(d);

			preparedStatement.setString(1, User.get_fname());
			preparedStatement.setString(2, User.get_mname());
			preparedStatement.setString(3, User.get_lname());
			preparedStatement.setDate(4, new java.sql.Date(date.getTime()));

			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("User with ID: " + User.get_user_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for ID: " + User.get_user_id());
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
	public void deleteUser(User User) {
		// Deletion
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM User where user_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, User.get_user_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("User with id " + User.get_user_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for id: " + User.get_user_id());
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
