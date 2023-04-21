package DAO_JDBC;

import DAO.CompanyDAO;

import Table.*;
import java.sql.*;


public class CompanyDAO_JDBC implements CompanyDAO {
    
	Connection dbConnection;

	public CompanyDAO_JDBC(Connection dbconn) {
		// JDBC driver name and database URL
		// Database credentials
		dbConnection = dbconn;
	}

	@Override
	public Company getCompanyByKey(int company_id) {
		Company c = new Company();
		String sql;
		Statement stmt = null;
		boolean flag = false;

		try {
			stmt = dbConnection.createStatement();
			sql = "select company_id,company_name from Company where company_id = " + company_id;
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set

			while (rs.next()) {
				// Retrieve by column name
				flag = true;
				int comp_id = rs.getInt("company_id");
				String company_name = rs.getString("company_name");
				c.set_company_id(comp_id);
				c.set_company_name(company_name);
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
	public void addCompany(Company company) throws Exception {
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "insert into Company(company_id,company_name) values (?,?)";

		

		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, company.get_company_id());
			preparedStatement.setString(2, company.get_company_name());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("company with ID " + company.get_company_id()
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
	public void updateCompany(Company company) throws Exception {

		PreparedStatement preparedStatement = null;
		String sql;

		sql = "update Company set company_name=? where company_id = "+ company.get_company_id();

		
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setString(1, company.get_company_name());

			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {

				System.out.println("company with comp_id: " + company.get_company_id()
						+ ", updated successfully");
			} else {
				System.out.println("Update Failed for comp_id: " + company.get_company_id());
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
	public void deleteCompany(Company company) {
		// Deletion
		PreparedStatement preparedStatement = null;
		String sql;
		sql = "DELETE FROM Company where company_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);

			preparedStatement.setInt(1, company.get_company_id());

			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			if (ret != 0) {
				System.out.println("company with ID " + company.get_company_id()
						+ " deleted from the database");
			} else {
				System.out.println("Delete Failed for ID: " + company.get_company_id());
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