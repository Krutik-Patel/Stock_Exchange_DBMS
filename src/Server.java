
import DAO.*;
import Table.*;
import Join_Table.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
public class Server {

	public static void main(String[] args) throws Exception {
		
		try{
		
			ServerSocket server = new ServerSocket(8888);
			int counter=0;
			System.out.println("Server Started ....");
			while(true) {
		
				counter++;
				Socket serverClient=server.accept();  //server accept the client connection request
				System.out.println(" >> " + "Client No:" + counter + " started!");
				ServerClientThread sct = new ServerClientThread(serverClient,counter); //send  the request to a separate thread
				sct.start();
			}
		
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class ServerClientThread extends Thread {
	public static DAO_Factory daoFactory;
	private static HashMap<Integer, ArrayList<Thrie>> buyerHash = new HashMap<>();
	private static HashMap<Integer, ArrayList<Thrie>> sellerHash = new HashMap<>();	
	private static int responses = 0;
	private static int revisePeriod = 20;
	Socket serverClient;
	int clientNo;
	int squre;

	ServerClientThread(Socket inSocket, int counter) {
		serverClient = inSocket;
		clientNo=counter;
	}


	public void run() {
	
		try {
			daoFactory = new DAO_Factory();
			DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
			DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
			String clientMessage="", serverMessage="";
			while(!clientMessage.equals("bye")) {
			
				/* CLIENT COMMUNICATION */
				clientMessage=inStream.readUTF();

				System.out.println("CLIENT --------------------------------");
				System.out.println(clientMessage);
				// SERVER MESSAGE
				serverMessage = parseResponse(clientMessage);

				System.out.println("SERVER ---------------------------------");
				System.out.println(serverMessage);

				outStream.writeUTF(serverMessage);
				outStream.flush();
				/**/
			}
			inStream.close();
			outStream.close();
			serverClient.close();
		} catch(Exception ex) {
			System.out.println(ex);
		} finally {
			System.out.println("Client -" + clientNo + " exit!! ");
		}
	}

		// Util Functions


	public static String parseResponse(String cliRes) throws Exception {
		responses++; // after revisePeriod number of responses, the indexStock calculates again
		if (responses % revisePeriod == 0) {
			reviseIndex();
			responses = 0;
		}
		try {
			String[] resp = cliRes.split(" ");
				
			
			if(resp[0].equals("1")) {
				int acc_id = Integer.parseInt(resp[1]);
				String pass = resp[2];
				
				String ret = loginAccount(acc_id,pass);

				return ret;
			}
			else if(resp[0].equals("2")){
				int holder_regs_id = Integer.parseInt(resp[1]);
				String password = resp[2];
				String bank_acc_no = resp[3];
				return registerAccount(password,holder_regs_id,bank_acc_no);

			}
			else if(resp[0].equals("3")){
				String fname = resp[1];
				String mname = resp[2];
				String lname = resp[3];
				String dob = resp[4];
				String panNo = resp[5];
				return registerUser(fname, mname, lname, dob, panNo);
			}
			else if(resp[0].equals("4")){
				String companyName = resp[1];
				String panNo = resp[2];
				return registerCompany(companyName, panNo);
			}
			else if(resp[0].equals("5")){
				int stockId = Integer.parseInt(resp[1]);
				String date = resp[2];
				return analyzeStock(stockId,date);
			}
			else if(resp[0].equals("6")){
				int accountId = Integer.parseInt(resp[1]);
				int stockId = Integer.parseInt(resp[2]);
				int stockUnits = Integer.parseInt(resp[3]);
				return buyStock(accountId, stockId, stockUnits);
			}
			else if(resp[0].equals("7")){
				int accountId = Integer.parseInt(resp[1]);
				int stockId = Integer.parseInt(resp[2]);
				int stockUnits = Integer.parseInt(resp[3]);
				return sellStock(accountId, stockId, stockUnits);
			}
			else if(resp[0].equals("8")){
				String param = resp[1];
				return getMarketTrend(param); 
			}
			else if(resp[0].equals("9")){
				int accountId = Integer.parseInt(resp[1]);
				return getTransactionHistory(accountId);
			}
			else if(resp[0].equals("10")){
				return "Nothing";
			}
		}
		catch(Exception e) {
			return "error";
		}
		return "No-result";
	}


	// DB Functions

	private static long generateChronoToken() {
		Calendar calendar = Calendar.getInstance();
        long token = calendar.get(Calendar.YEAR) * (long)10e12;
        token += calendar.get(Calendar.MONTH)* 10e10;
        token += calendar.get(Calendar.DATE) * 10e8;
        token += calendar.get(Calendar.HOUR) * 10e6;
        token += calendar.get(Calendar.MINUTE) * 10e4;
        token += calendar.get(Calendar.SECOND) * 10e2;
        token += calendar.get(Calendar.MILLISECOND);
		return token;
	}

	private static float generatePriceMultiplier(long lessTkn, long moreTkn) {
		return 1;
	}

	private static int generateParticipantID() {
		try {
			daoFactory.activateConnection();
			int generatedID = daoFactory.getParticipantDAO().getNextID() + 1;			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return generatedID;
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return -1;
		}
	}

	private static int generateAccountID() {
		try {
			daoFactory.activateConnection();
			int generatedID = daoFactory.getAccountsDAO().getNextID() + 1;			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return generatedID;
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return -1;
		}
	}
		//end main
	public static String registerUser(String fname, String mname, String lname, String dob, String panNo) throws Exception {
		Date currDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		Date dateOfBirth = sdf.parse(dob);
		int participantNum = generateParticipantID();
		Participants newParticipant = new Participants(participantNum, panNo, currDate);
		User newUser = new User(participantNum, fname, mname, lname, dateOfBirth);
		try {

			// Start transaction boundary
			daoFactory.activateConnection();

			// Insert participant
			ParticipantDAO sdao = daoFactory.getParticipantDAO();
			UserDAO udao = daoFactory.getUserDAO();
			sdao.addParticipant(newParticipant);
			udao.addUser(newUser);
			// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			return "YES "+String.valueOf(participantNum);
		} catch(Exception e){
				// End transaction boundary with failure
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();

			return String.valueOf(-1);
		}
	}
		

	public static String registerCompany(String companyName, String panNo)throws Exception{
		Date currDate = new Date();
		int participantNum = generateParticipantID();
		Participants newParticipant = new Participants(participantNum, panNo, currDate);
		Company newCompany = new Company(participantNum, companyName);
		try {
			// Start transaction boundary
			daoFactory.activateConnection();

			// Insert participant
			ParticipantDAO sdao = daoFactory.getParticipantDAO();

			CompanyDAO cdao = daoFactory.getCompanyDAO();
			sdao.addParticipant(newParticipant);
			cdao.addCompany(newCompany);

			// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			return "YES "+String.valueOf(participantNum);
		} catch(Exception e){
				// End transaction boundary with failure
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();

			return String.valueOf(-1);
		}	
	}


		// ACCOUNT LOGIN and REGISTRATION
	public static String registerAccount(String password, int holder_regs_id,String bank_acc_no)throws Exception{

		int accountNum = generateAccountID();
		Accounts newAccount = new Accounts(accountNum,holder_regs_id,password,bank_acc_no);
		
		try {
			// Start transaction boundary
			daoFactory.activateConnection();

			// Insert participant
			AccountsDAO sdao = daoFactory.getAccountsDAO();

			sdao.addAccount(newAccount);

			// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		} catch(Exception e){
				// End transaction boundary with failure
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "NO";
		}

		return "YES "+String.valueOf(accountNum);
	}


	public static String loginAccount(int account_id,String password) throws Exception{

		try {
			// Start transaction boundary
			daoFactory.activateConnection();

			// Insert participant
			AccountsDAO sdao = daoFactory.getAccountsDAO();

			Accounts loginAcc = sdao.getAccountByKey(account_id);

			if(loginAcc.get_password().equals(password)){
				// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
				return "YES";
			}
			else return "NO";
			

			
		} catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
				return "NO";
		}
	}


	
	public static String getMarketTrend(String par) throws Exception{
		try{
		
			daoFactory.activateConnection();
			
			StockDAO sdao = daoFactory.getStockDAO();
			Date endD = new Date(); // end Date 
			Date stD;  // Start Date
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endD);
			if (par.equals("M")) {
				calendar.add(calendar.MONTH, -1);
			} else if (par.equals("D")) {
				calendar.add(calendar.DAY_OF_MONTH, -1);
			} else if (par.equals("Y")) {
				calendar.add(calendar.YEAR, -1);
			} else if (par.equals("W")) {
				calendar.add(calendar.DAY_OF_MONTH, -7);
			}
			stD = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = sdf.format(endD);
			String stDate = sdf.format(stD);
			ArrayList<Market_Trend> mark_trend = sdao.get_market_trend(stDate, endDate);
			
			String result = "YES ";
			for (Market_Trend t : mark_trend) {
				// System.out.println("Stock Name:" + t.get_stock_name());	
				// System.out.println("Stock ID:" + t.get_stock_id());	
				// System.out.println("Prev price:" + t.get_prev_price());	
				// System.out.println("Curr price:" + t.get_curr_price());	

				result += t.get_stock_name();
				result += " ";
				result += String.valueOf(t.get_stock_id());
				result += " ";
				result += String.valueOf(t.get_prev_price());
				result += " ";
				result += String.valueOf(t.get_curr_price());
				result += " ";
			}
			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return result;

		} catch(Exception e){
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}
	}
		
	public static String getTransactionHistory(int account_id)throws Exception{
		try{
			daoFactory.activateConnection();
		// ParticipantDAO pdao = daoFactory.getParticipantDAO();
			TransactionDAO sdao = daoFactory.getTransactionDAO();
			ArrayList<Transaction_History> th = sdao.getTransactionHistory(account_id);
			String result = "YES ";
			if(th != null){
				for(Transaction_History element : th) {
					Transaction t = element.get_trans();

					result += String.valueOf(t.get_trans_id());
					result += " ";
					result += t.get_trans_date();
					result += " ";
					result += String.valueOf(t.get_units());
					result += " ";
					result += String.valueOf(t.get_trans_price());
					result += " ";
					result += element.get_from_name();
					result += " ";
					result += element.get_to_name();
					result += " ";
					result += element.get_stock_name();
					result += " ";
					
				}
			
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return result;

		}catch(Exception e){
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}

	}

	public static String indexStockInfo() throws Exception{
		return "";
	}


	public static boolean reviseIndex() {
		try {
			daoFactory.activateConnection();
			IndexStockDAO idao = daoFactory.getIndexStockDAO();

			idao.recalculate();

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			return true;
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return false;
		}
	}

	public static String analyzeStock(int stockId, String date) throws Exception{
		try{
			daoFactory.activateConnection();
			StockDAO sdao = daoFactory.getStockDAO();
			Stock_Analysis s1 = sdao.get_stock_analysis(stockId,date);
				
			String result = "YES ";
			if(s1 != null){
				Stock st = s1.get_stock();
				result += "Stock Name: ";
				result += st.get_stock_name();
				result += " ";
				result += "Stock Units: ";
				result += String.valueOf(st.get_stock_units());
				result += " ";
				result += "Stock Price: ";
				result += String.valueOf(st.get_stock_price());
				result += " ";
				result += "Stock Domain ID: ";
				result += String.valueOf(st.get_dom_id());
				result += " ";
				result += "Transacted money from ";
				result += date;
				result += ": ";
				result += String.valueOf(s1.get_total_trans_price());
				result += " ";
				result += "Transacted units in past month: ";
				result += String.valueOf(s1.get_total_units());
				result += " ";
				result += "Total number of transactions in past month: ";
				result += String.valueOf(s1.get_total_transaction());
				result += " ";
			}
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return result;

		}catch(Exception e){
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}

		//return "error";
	}


	/* BUY _ SELL */


	public static String buyStock(int accountID,int  stockId,int stockUnits) throws Exception{
		try {
			daoFactory.activateConnection();
			TransactionDAO tdao = daoFactory.getTransactionDAO();
			StockDAO sdao = daoFactory.getStockDAO();
			StocksOwnershipDAO sodao = daoFactory.getStocksOwnershipDAO();
			
			float stkprice = sdao.getStockByKey(stockId).get_stock_price();
			long userToken = generateChronoToken();

			int qtyBought = 0;
			if (!sellerHash.containsKey(stockId)) {
				ArrayList<Thrie> newLst = new ArrayList<>();
				sellerHash.put(stockId, newLst);
			} 
			if (!sellerHash.get(stockId).isEmpty()) {
				ArrayList<Thrie> selLst = sellerHash.get(stockId);
				while (qtyBought < stockUnits && !selLst.isEmpty()) {
					Thrie seller = selLst.get(0);
					if ((stockUnits - qtyBought) < seller.getStockUnits()) {
						int avail = seller.getStockUnits();
						selLst.get(0).setStockUnits(avail - (stockUnits - qtyBought));
						
						stkprice *= generatePriceMultiplier(seller.getChronoToken(), userToken);

						Transaction newTrans = new Transaction(0, seller.getaccountID(), accountID, stockId, (stockUnits - qtyBought), new Date(), stkprice * (stockUnits - qtyBought));
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, seller.getaccountID(), (avail - (stockUnits - qtyBought)));
						sodao.updateStockOwnership(nwsoshp);
						
						qtyBought = stockUnits;
						
						break;
					} else if ((stockUnits - qtyBought) >= seller.getStockUnits()) {
						qtyBought += seller.getStockUnits();
						int qt = Math.min(seller.getStockUnits(), (stockUnits - qtyBought));

						stkprice *= generatePriceMultiplier(seller.getChronoToken(), userToken);

						Transaction newTrans = new Transaction(0, seller.getaccountID(), accountID, stockId, qt, new Date(), stkprice * qt);
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, seller.getaccountID(), 0);
						sodao.deleteStockOwnership(nwsoshp);
						selLst.remove(0);
					}
				}
				// price update
				Stock nwStk = sdao.getStockByKey(stockId);
				nwStk.set_stock_price(stkprice);
				sdao.updateStock(nwStk);

				// stocks ownership updates
				StocksOwnership buyerSO = sodao.getStocksOnwershipByKey(stockId, accountID);
				if (buyerSO != null) {
					buyerSO.set_units_owned(buyerSO.get_units_owned() + qtyBought);
					sodao.updateStockOwnership(buyerSO);
				} else {
					buyerSO = new StocksOwnership(stockId, accountID, qtyBought);
					sodao.addStocksOwnership(buyerSO);
				}
			} if(sellerHash.get(stockId).isEmpty() && qtyBought < stockUnits) {
				ArrayList<Thrie> buyLst = buyerHash.get(stockId);
				Thrie newBuyer = new Thrie(stockUnits - qtyBought, accountID, userToken);
				buyLst.add(stockId, newBuyer);
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			return "Stocks bought: " + qtyBought + "\nStocks Remaining to buy" + (stockUnits - qtyBought) + "\n";
			
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}
	}

	public static String sellStock(int accountID,int  stockId,int stockUnits) throws Exception{
		try {
			String initStr = "";
			daoFactory.activateConnection();
			TransactionDAO tdao = daoFactory.getTransactionDAO();
			StockDAO sdao = daoFactory.getStockDAO();
			StocksOwnershipDAO sodao = daoFactory.getStocksOwnershipDAO();
			
			float stkprice = sdao.getStockByKey(stockId).get_stock_price();
			long userToken = generateChronoToken();

			StocksOwnership stkOnshp = sodao.getStocksOnwershipByKey(stockId, stockUnits);
			if (stkOnshp == null) {
				return "You don't own any such stock.";
			} else if (stkOnshp.get_units_owned() < stockUnits) {
				stockUnits = stkOnshp.get_units_owned();
				initStr = "You own lesser that asked. Only can sell " + stockUnits + "units of stocks.\n";
			}


			int qtySold = 0;
			if (!buyerHash.containsKey(stockId)) {
				ArrayList<Thrie> newLst = new ArrayList<>();
				buyerHash.put(stockId, newLst);
			} if (!buyerHash.get(stockId).isEmpty()) {
				ArrayList<Thrie> buyLst = buyerHash.get(stockId);
				while (qtySold < stockUnits && !buyLst.isEmpty()) {
					Thrie buyer = buyLst.get(0);
					if ((stockUnits - qtySold) < buyer.getStockUnits()) {
						int avail = buyer.getStockUnits();
						buyer.setStockUnits(avail - (stockUnits - qtySold));
					

						stkprice *= generatePriceMultiplier(userToken, buyer.getChronoToken());

						Transaction newTrans = new Transaction(0, accountID, buyer.getaccountID(), stockId, (stockUnits - qtySold), new Date(), stkprice * (stockUnits - qtySold));
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), (avail - (stockUnits - qtySold)));
						sodao.updateStockOwnership(nwsoshp);
						
						qtySold = stockUnits;
						
						break;
					} else if ((stockUnits - qtySold) >= buyer.getStockUnits()) {
						qtySold += buyer.getStockUnits();
						int qt = Math.min(buyer.getStockUnits(), (stockUnits - qtySold));

						stkprice *= generatePriceMultiplier(userToken, buyer.getChronoToken());

						Transaction newTrans = new Transaction(0, accountID, buyer.getaccountID(), stockId, qt, new Date(), stkprice * qt);
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), 0);
						sodao.deleteStockOwnership(nwsoshp);
						buyLst.remove(0);
					}

				}
				// price update
				Stock nwStk = sdao.getStockByKey(stockId);
				nwStk.set_stock_price(stkprice);
				sdao.updateStock(nwStk);
				
				// stock ownership updates
				StocksOwnership sellerSO = sodao.getStocksOnwershipByKey(stockId, accountID);
				if (sellerSO.get_units_owned() > qtySold) {
					sellerSO.set_units_owned(sellerSO.get_units_owned() - qtySold);
					sodao.updateStockOwnership(sellerSO);
				} else {
					sodao.deleteStockOwnership(sellerSO);
				}
			} if (qtySold < stockUnits && buyerHash.get(stockId).isEmpty()) {
				ArrayList<Thrie> selLst = sellerHash.get(stockId);
				Thrie newsel = new Thrie(stockUnits - qtySold, accountID, userToken);
				selLst.add(stockId, newsel);
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			return initStr + "Stocks bought: " + qtySold + "\nStocks Remaining to buy" + (stockUnits - qtySold) + "\n";
			
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}
	}
}