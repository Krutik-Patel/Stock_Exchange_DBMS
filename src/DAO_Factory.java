import DAO.*;
import DAO_JDBC.*;

import java.sql.*;

/*
	Methods to be called in the following order:

	1. activateConnection
	2. 	Any number getDAO calls with any number of database transactions
	3. deactivateConnection
*/
public class DAO_Factory {

	public enum TXN_STATUS {
		COMMIT, ROLLBACK
	};

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/stockdb?characterEncoding=latin1&useConfigs=maxPerformance";
	static final String USER = "root";
	static final String PASS = "password";
	Connection dbconnection = null;

	// You can add additional DAOs here as needed
	ParticipantDAO participantDAO = null;
	StockDAO stockDAO = null;
	CompanyDAO companyDAO = null;
	UserDAO userDAO = null;
	TransactionDAO transactionDAO = null;
	AccountsDAO accountsDAO = null;
	StocksOwnershipDAO stocksOwnershipDAO = null;
	IndexStockDAO indexStockDAO = null;

	boolean activeConnection = false;

	public DAO_Factory() {
		dbconnection = null;
		activeConnection = false;
	}

	public void activateConnection() throws Exception {
		if (activeConnection == true)
			throw new Exception("Connection already active");

		System.out.println("Connecting to database...");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbconnection = DriverManager.getConnection(DB_URL, USER, PASS);
			dbconnection.setAutoCommit(false);
			activeConnection = true;
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}


	// DAO

	public ParticipantDAO getParticipantDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (participantDAO == null)
			participantDAO = new ParticipantDAO_JDBC(dbconnection);

		return participantDAO;
	}

	public StockDAO getStockDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (stockDAO == null)
			stockDAO = new StockDAO_JDBC(dbconnection);

		return stockDAO;
	}

	public CompanyDAO getCompanyDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (companyDAO == null)
			companyDAO = new CompanyDAO_JDBC(dbconnection);

		return companyDAO;
	}

	public UserDAO getUserDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (userDAO == null)
			userDAO = new UserDAO_JDBC(dbconnection);

		return userDAO;
	}

	public TransactionDAO getTransactionDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (transactionDAO == null)
			transactionDAO = new TransactionDAO_JDBC(dbconnection);

		return transactionDAO;
	}

	public AccountsDAO getAccountsDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (accountsDAO == null)
			accountsDAO = new AccountsDAO_JDBC(dbconnection);

		return accountsDAO;
	}

	public StocksOwnershipDAO getStocksOwnershipDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (stocksOwnershipDAO == null)
			stocksOwnershipDAO = new StocksOwnershipDAO_JDBC(dbconnection);

		return stocksOwnershipDAO; 
				
	}

	public IndexStockDAO getIndexStockDAO() throws Exception {
		if (activeConnection == false)
			throw new Exception("Connection not activated...");

		if (indexStockDAO == null) 
			indexStockDAO = new IndexStockDAO_JDBC(dbconnection);

		return indexStockDAO;
	}

	// END

	public void deactivateConnection(TXN_STATUS txn_status) {
		// Okay to keep deactivating an already deactivated connection
		activeConnection = false;
		if (dbconnection != null) {
			try {
				if (txn_status == TXN_STATUS.COMMIT)
					dbconnection.commit();
				else
					dbconnection.rollback();

				dbconnection.close();
				dbconnection = null;

				// Nullify all DAO objects
				participantDAO = null;
				stockDAO = null;
				userDAO = null;
				companyDAO = null;
				transactionDAO = null;
				accountsDAO = null;
				stocksOwnershipDAO = null;
				indexStockDAO=null;
			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
};
