
import DAO.*;
import Table.*;
import Join_Table.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	private static HashMap<Integer, ArrayList<Thrie>> buyerHash = new HashMap<>();
	private static HashMap<Integer, ArrayList<Thrie>> sellerHash = new HashMap<>();
	private static RespObj respObj = new RespObj();	
	

	public static void main(String[] args) throws Exception {
		
		try{
		
			ServerSocket server = new ServerSocket(8888);
			int counter=0;
			System.out.println("Server Started ....");
			while(true) {
		
				counter++;
				Socket serverClient=server.accept();  //server accept the client connection request
				System.out.println(" >> " + "Client No:" + counter + " started!");
				ServerClientThread sct = new ServerClientThread(serverClient,counter, buyerHash, sellerHash, respObj); //send  the request to a separate thread
				sct.start();
			}
		
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class ServerClientThread extends Thread {
	public static DAO_Factory daoFactory;
	private static int revisePeriod = 2;
	private static HashMap<Integer, ArrayList<Thrie>> buyerHash;
	private static HashMap<Integer, ArrayList<Thrie>> sellerHash;	
	private static RespObj resp;
	Socket serverClient;
	int clientNo;
	int squre;

	ServerClientThread(Socket inSocket, int counter, HashMap<Integer, ArrayList<Thrie>> buyerHash, HashMap<Integer, ArrayList<Thrie>> sellerHash, RespObj resp) {
		serverClient = inSocket;
		clientNo=counter;
		this.buyerHash = buyerHash;
		this.sellerHash = sellerHash;
		this.resp = resp;
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

				System.out.println("CLIENT ----------------------------------");
				System.out.println(clientMessage);
				// SERVER MESSAGE
				serverMessage = parseResponse(clientMessage);

				System.out.println("SERVER ----------------------------------");
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
			System.out.println("Client " + clientNo + " exit!! ");
		}
	}

		// Util Functions
	private static void respCheck() {
		resp.setResponses(resp.getResponses() + 1); // after revisePeriod number of responses, the indexStock calculates again
		if (resp.getResponses() % revisePeriod == 0) {
			reviseIndex();
			resp.setResponses(0);
		}
	}

	public static String parseResponse(String cliRes) throws Exception {
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
				respCheck();
				int accountId = Integer.parseInt(resp[1]);
				int stockId = Integer.parseInt(resp[2]);
				int stockUnits = Integer.parseInt(resp[3]);
				return buyStock(accountId, stockId, stockUnits);
			}
			else if(resp[0].equals("7")){
				respCheck();
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
				return indexStockInfo();
			}
		}
		catch(Exception e) {
			return "error";
		}
		return "No result";
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
		long avg_tkn = lessTkn + (moreTkn - lessTkn) / 2;
		double ration = Math.atan((lessTkn - moreTkn) / Math.sqrt(Math.sqrt(avg_tkn)));
		float ratio = (float) (1 + ration);
		return ratio;
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
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

			return "YES$"+String.valueOf(participantNum);
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

			return "YES$"+String.valueOf(participantNum);
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

		return "YES$"+String.valueOf(accountNum);
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
			
			String result = "YES$++++++++++++++++++++++++++++++++++++++++$";
			for (Market_Trend t : mark_trend) {
				// System.out.println("Stock Name:" + t.get_stock_name());	
				// System.out.println("Stock ID:" + t.get_stock_id());	
				// System.out.println("Prev price:" + t.get_prev_price());	
				// System.out.println("Curr price:" + t.get_curr_price());	

				result += "STOCK: ";
				result += t.get_stock_name();
				result += "\nSTOCK ID: ";
				result += String.valueOf(t.get_stock_id());
				result += "\nSTOCK PREV PRICE: ";
				result += String.valueOf(t.get_prev_price());
				result += "\nSTOCK CURRENT PRICE: ";
				result += String.valueOf(t.get_curr_price());
				result +="\nPercent Change in STOCK PRICE: ";
				result += (((t.get_curr_price()-t.get_prev_price())/t.get_prev_price())*100);
				result += "%";
				result += "$++++++++++++++++++++++++++++++++++++++++$";
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
			String result = "YES$++++++++++++++++++++++++++++++++++++++++$";
			if(th != null){
				for(Transaction_History element : th) {
					Transaction t = element.get_trans();

					result += "Trans ID: ";
					result += String.valueOf(t.get_trans_id());
					result += "$";
					result += "Trans Date: ";
					result += t.get_trans_date();
					result += "$";
					result += "Trans Units: ";
					result += String.valueOf(t.get_units());
					result += "$";
					result += "Trans Price: ";
					result += String.valueOf(t.get_trans_price());
					result += "$";
					result += "Trans FROM: ";
					result += element.get_from_name();
					result += "$";
					result += "Trans TO: ";
					result += element.get_to_name();
					result += "$";
					result += "Trans STOCK: ";
					result += element.get_stock_name();
					result +="$++++++++++++++++++++++++++++++++++++++++$";
					
				}
			
			}
			else{
				result += "No record found";
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return result;

		}catch(Exception e){
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}

	}

	


	public static boolean reviseIndex() {
		try {
			daoFactory.activateConnection();
			IndexStockDAO idao = daoFactory.getIndexStockDAO();

			idao.recalculate();

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			System.out.println("Index Table recalculated...");
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
				
			String result = "YES$++++++++++++++++++++++++++++++++++++++++$";
			if(s1 != null){
				Stock st = s1.get_stock();
				result += "Stock Name: ";
				result += st.get_stock_name();
				result += "$";
				result += "Stock Units: ";
				result += String.valueOf(st.get_stock_units());
				result += "$";
				result += "Stock Price: ";
				result += String.valueOf(st.get_stock_price());
				result += "$";
				result += "Stock Domain ID: ";
				result += String.valueOf(st.get_dom_id());
				result += "$";
				result += "Transacted money from ";
				result += date;
				result += ":  ";
				result += String.valueOf(s1.get_total_trans_price());
				result += "$";
				result += "Transacted units : ";
				result += String.valueOf(s1.get_total_units());
				result += "$";
				result += "Total number of transactions from ";
				result += date;
				result += ": ";
				result += String.valueOf(s1.get_total_transaction());
				result += "$++++++++++++++++++++++++++++++++++++++++$";
			}
			else{
				result+= "No record found";
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
						int qt = seller.getStockUnits();
						System.out.println("StockUnits :" + qt);
						System.out.println("AccountID :" + seller.getaccountID());
						System.out.println("BuyerAccountID :" + accountID);
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
				if (buyLst == null) {
					buyLst = new ArrayList<>();
				}
				Thrie newBuyer = new Thrie(stockUnits - qtyBought, accountID, userToken);
				buyLst.add(newBuyer);
				buyerHash.put(stockId, buyLst);
				System.out.println("buyer added to waiting list...");
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			if (qtyBought > 0) {
				return "YES$"+"Stocks bought: " + qtyBought + "\nStocks Remaining to buy :" + (stockUnits - qtyBought) + "\n";
			} else {
				return "YES$Stock not available currently, your transaction will happen automatically when a seller is available.";
			}
			
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

			StocksOwnership stkOnshp = sodao.getStocksOnwershipByKey(stockId, accountID);
			if (stkOnshp == null) {
				return "YES$You don't own any such stock.";
			} else if (stkOnshp.get_units_owned() < stockUnits) {
				stockUnits = stkOnshp.get_units_owned();
				initStr = "You own lesser than asked. Only can sell " + stockUnits + "units of stocks.\n";
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
						StocksOwnership nwsoshp = sodao.getStocksOnwershipByKey(stockId, accountID);
						if (nwsoshp != null) {
							nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), (avail - (stockUnits - qtySold)));
							sodao.updateStockOwnership(nwsoshp);
						} else {
							nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), (avail - (stockUnits - qtySold)));
							sodao.addStocksOwnership(nwsoshp);
						}
						
						
						qtySold = stockUnits;
						
						break;
					} else if ((stockUnits - qtySold) >= buyer.getStockUnits()) {
						qtySold += buyer.getStockUnits();
						int qt = buyer.getStockUnits();

						stkprice *= generatePriceMultiplier(userToken, buyer.getChronoToken());

						Transaction newTrans = new Transaction(0, accountID, buyer.getaccountID(), stockId, qt, new Date(), stkprice * qt);
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), qt);
						sodao.addStocksOwnership(nwsoshp);
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
				if (selLst == null) {
					selLst = new ArrayList<>();
				}
				Thrie newsel = new Thrie(stockUnits - qtySold, accountID, userToken);
				selLst.add(newsel);
				sellerHash.put(stockId, selLst);
				System.out.println("seller added to waiting list...");
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

			if (qtySold > 0) {
				return "YES$"+initStr + "Stocks bought: " + qtySold + "\nStocks Remaining to buy: " + (stockUnits - qtySold) + "\n";
			} else {
				return "YES$" + initStr + "No buyer available currently, your transaction will occur automatically when a buyer for the stock is available.";
			}
			
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}
	}

// indexStockInfo
	public static String indexStockInfo() throws Exception{
		try{
			daoFactory.activateConnection();
			IndexStockDAO sdao = daoFactory.getIndexStockDAO();
			ArrayList<Index_Info> stkinfo = sdao.getIndexStockInfo();
			String result = "YES$++++++++++++++++++++++++++++++++++++++++$";
			if(stkinfo != null){
				for(Index_Info ind : stkinfo) {
					
					result += "Index Stock ID: ";
					result += String.valueOf(ind.get_index_stk_id());
					result += "$Index Name: ";
					result += ind.get_stock_name();
					result += "$Index Stock Units: ";
					result += String.valueOf(ind.get_stock_units());
					result += "$Index Price: ";
					result += String.valueOf(ind.get_stock_price());
					result += "$Index Domain ID: ";
					result += String.valueOf(ind.get_dom_id());
					result += "$Index Domain Name: ";
					result += ind.get_domain_name();
					result += "$Index Quantity: ";
					result += String.valueOf(ind.get_stock_quant());
					result += " ";
					result +="$++++++++++++++++++++++++++++++++++++++++$";
					
				}
			
			}
			else{
				
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return result;

		}catch(Exception e){
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return "error";
		}
	}



}