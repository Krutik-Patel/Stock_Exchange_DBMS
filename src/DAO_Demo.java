
import DAO.*;
import Table.*;
import Join_Table.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class DAO_Demo {
	private static DAO_Factory daoFactory;
	private static HashMap<Integer, ArrayList<Thrie>> buyerHash = new HashMap<>();
	private static HashMap<Integer, ArrayList<Thrie>> sellerHash = new HashMap<>();
	private static int responces = 0;

	public static void initialize() throws Exception {
		try{

			daoFactory = new DAO_Factory();
			//inputHandler();

		}catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
		}
	}

	private static int generateChronoToken() {
		return 0;
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

			return String.valueOf(participantNum);
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

			return String.valueOf(participantNum);
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
		}

		return String.valueOf(accountNum);
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
				return "true";
			}
			else return "false";
			

			
		} catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
				return "false";
		}
	}

	public static String analyzeStock(int stockId, String date) throws Exception{
		try{
			daoFactory.activateConnection();
			StockDAO sdao = daoFactory.getStockDAO();
			Stock_Analysis s1 = sdao.get_stock_analysis(stockId,date);
				
			String result = "";
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

	public static String buyStock(int accountID, int stockId, int stockUnits) throws Exception{
		try {
			daoFactory.activateConnection();
			TransactionDAO tdao = daoFactory.getTransactionDAO();
			StockDAO sdao = daoFactory.getStockDAO();
			StocksOwnershipDAO sodao = daoFactory.getStocksOwnershipDAO();
			
			float stkprice = sdao.getStockByKey(stockId).get_stock_price();

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
					
						Transaction newTrans = new Transaction(0, seller.getaccountID(), accountID, stockId, (stockUnits - qtyBought), new Date(), stkprice * (stockUnits - qtyBought));
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, seller.getaccountID(), (avail - (stockUnits - qtyBought)));
						sodao.updateStockOwnership(nwsoshp);
						
						qtyBought = stockUnits;
						
						break;
					} else if ((stockUnits - qtyBought) >= seller.getStockUnits()) {
						qtyBought += seller.getStockUnits();
						int qt = Math.min(seller.getStockUnits(), (stockUnits - qtyBought));
						Transaction newTrans = new Transaction(0, seller.getaccountID(), accountID, stockId, qt, new Date(), stkprice * qt);
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, seller.getaccountID(), 0);
						sodao.deleteStockOwnership(nwsoshp);
						selLst.remove(0);
					}
					StocksOwnership buyerSO = sodao.getStocksOnwershipByKey(stockId, accountID);
					if (buyerSO != null) {
						buyerSO.set_units_owned(buyerSO.get_units_owned() + qtyBought);
						sodao.updateStockOwnership(buyerSO);
					} else {
						buyerSO = new StocksOwnership(stockId, accountID, qtyBought);
						sodao.addStocksOwnership(buyerSO);
					}
				}
			} else {
				ArrayList<Thrie> buyLst = buyerHash.get(stockId);
				int chronoToken = generateChronoToken();
				Thrie newBuyer = new Thrie(stockId, accountID, chronoToken);
				buyLst.add(stockUnits, newBuyer);
			}
	
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
	
			return "Stocks bought: " + qtyBought + "\nStocks Remaining to buy" + (stockUnits - qtyBought) + "\n";
			
		} catch (Exception e) {
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
				return "error";
		}
		
	}

	public static String sellStock(int accountID, int stockId, int stockUnits) throws Exception {
		try {
			String initStr = "";
			daoFactory.activateConnection();
			TransactionDAO tdao = daoFactory.getTransactionDAO();
			StockDAO sdao = daoFactory.getStockDAO();
			StocksOwnershipDAO sodao = daoFactory.getStocksOwnershipDAO();
			
			float stkprice = sdao.getStockByKey(stockId).get_stock_price();

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
				while (qtySold < stockUnits || !buyLst.isEmpty()) {
					Thrie buyer = buyLst.get(0);
					if ((stockUnits - qtySold) < buyer.getStockUnits()) {
						int avail = buyer.getStockUnits();
						buyer.setStockUnits(avail - (stockUnits - qtySold));
					
						Transaction newTrans = new Transaction(0, accountID, buyer.getaccountID(), stockId, (stockUnits - qtySold), new Date(), stkprice * (stockUnits - qtySold));
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), (avail - (stockUnits - qtySold)));
						sodao.updateStockOwnership(nwsoshp);
						
						qtySold = stockUnits;
						
						break;
					} else if ((stockUnits - qtySold) >= buyer.getStockUnits()) {
						qtySold += buyer.getStockUnits();
						int qt = Math.min(buyer.getStockUnits(), (stockUnits - qtySold));
						Transaction newTrans = new Transaction(0, accountID, buyer.getaccountID(), stockId, qt, new Date(), stkprice * qt);
						tdao.addTransaction(newTrans);
						StocksOwnership nwsoshp = new StocksOwnership(stockId, buyer.getaccountID(), 0);
						sodao.deleteStockOwnership(nwsoshp);
						buyLst.remove(0);
					}

					StocksOwnership sellerSO = sodao.getStocksOnwershipByKey(stockId, accountID);
					if (sellerSO.get_units_owned() > qtySold) {
						sellerSO.set_units_owned(sellerSO.get_units_owned() - qtySold);
						sodao.updateStockOwnership(sellerSO);
					} else {
						sodao.deleteStockOwnership(sellerSO);
					}
				}
			} else {
				ArrayList<Thrie> buyLst = buyerHash.get(stockId);
				int chronoToken = generateChronoToken();
				Thrie newBuyer = new Thrie(stockId, accountID, chronoToken);
				buyLst.add(stockUnits, newBuyer);
			}
	
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
	
			return initStr + "Stocks bought: " + qtySold + "\nStocks Remaining to buy" + (stockUnits - qtySold) + "\n";
			
		} catch (Exception e) {
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
				return "error";
		}
		

	}

	public static String getMarketTrend(String par) throws Exception{
		try{
		
			daoFactory.activateConnection();
			StockDAO sdao = daoFactory.getStockDAO();

			String startDate = "", endDate = "";
			//yaha par modify karana hai
			ArrayList<Market_Trend> mark_trend = sdao.get_market_trend(startDate, endDate);
			
			String result = "";
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
			String result = "";
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
	
	public static String parseResponse(String cliRes) throws Exception{
		responces++;
		try{
			String[] resp = cliRes.split(" ");
			if(resp[0].equals("1")){
				String fname = resp[1];
				String mname = resp[2];
				String lname = resp[3];
				String dob = resp[4];
				String panNo = resp[5];
				return registerUser(fname, mname, lname, dob, panNo);
			}
			else if(resp[0].equals("2")){
				String companyName = resp[1];
				String panNo = resp[2];
				return registerCompany(companyName, panNo);
			}
			else if(resp[0].equals("3")){
				int stockId = Integer.parseInt(resp[1]);
				String date = resp[2];
				return analyzeStock(stockId, date);
			}
			else if(resp[0].equals("4")){
				int accountId = Integer.parseInt(resp[1]);
				int stockId = Integer.parseInt(resp[2]);
				int stockUnits = Integer.parseInt(resp[3]);
				return buyStock(accountId, stockId, stockUnits);
			}
			else if(resp[0].equals("5")){
				int accountId = Integer.parseInt(resp[1]);
				int stockId = Integer.parseInt(resp[2]);
				int stockUnits = Integer.parseInt(resp[3]);
				return sellStock(accountId, stockId, stockUnits);
			}
			else if(resp[0].equals("6")){
				String param = resp[1];
				return getMarketTrend(param); 
			}
			else if(resp[0].equals("7")){
				int accountId = Integer.parseInt(resp[1]);
				return getTransactionHistory(accountId);
			}
			else if(resp[0].equals("8")){
				return indexStockInfo();
			}
		}
		catch(Exception e){
			return "error";
		}
		return "No-result";
	}

	//Input handler
	// public static void inputHandler() throws Exception {

	// 	int login_acc_id = -1;



	// 	//System.out.println();
	// 	Scanner sc = new Scanner(System.in);

	// 	while(true){

	// 		System.out.print("Already have an account (y / n): ");
	// 		String register = sc.next();

	// 		if(register.equals("y")){
	// 			System.out.print("Account ID : ");
	// 			int acc_id = sc.nextInt();
	// 			System.out.print("Password :");
	// 			String pass = sc.next();
				
	// 			if( loginAccount(acc_id,pass)){
	// 				login_acc_id = acc_id;
	// 				break;
	// 			}
	// 			else{
	// 				System.out.println(" -- Wrong ID or PASSWORD --");
	// 				continue;
	// 			} 
					
	// 		}
	// 		else{

	// 			int holder_regs_id = -1;
	// 			System.out.print("Register User (u) / Company (c): ");
	// 			String part_choice = sc.next();
	// 			if(part_choice.equals("u")){
	// 					// 1 - Register User
	// 				System.out.print("First name : ");
	// 				String fname = sc.next();
	// 				System.out.print("Middle name : ");
	// 				String mname = sc.next();
	// 				System.out.print("Last name : ");
	// 				String lname = sc.next();
	// 				System.out.print("Date of birth : ");
	// 				String dob = sc.next();
	// 				System.out.print("Pan number : ");
	// 				String panNo = sc.next();
	// 				holder_regs_id = registerUser(fname, mname, lname, dob, panNo);

					
				
	// 			}
	// 			else if(part_choice.equals("c")){
	// 					// 2 - Register Company
	// 				System.out.print("Company name : ");
	// 				String companyName = sc.next();
	// 				System.out.print("Pan number : ");
	// 				String panNo2 = sc.next();
	// 				holder_regs_id = registerCompany(companyName, panNo2);
				
	// 			}

	// 			else continue;

	// 			if(holder_regs_id == -1){
	// 				continue;
	// 			}
				

	// 			System.out.println(" -- SignUP Account --");
	// 			System.out.print("SET Password: ");
	// 			String password = sc.next();
	// 			System.out.print("Bank Account no.: ");
	// 			String bank_acc_no = sc.next();
	// 			int acc_id = registerAccount(password, holder_regs_id, bank_acc_no);

	// 			System.out.println(" -- Your Account ID: "+acc_id+" --");
	// 			if( loginAccount(acc_id,password)){
	// 				login_acc_id = acc_id;
	// 				break;
	// 			}
	// 			else{
	// 				System.out.println(" -- Wrong ID or PASSWORD --");
	// 				continue;
	// 			} 
	// 		}

			

			
	// 	}


	// 	System.out.print("Usecase Type : ");

	// 	int usecaseType = sc.nextInt();
	// 	switch(usecaseType){
	// 		case 1 :
	// 		// 1 - Register User
	// 			System.out.print("First name : ");
	// 			String fname = sc.next();
	// 			System.out.print("Middle name : ");
	// 			String mname = sc.next();
	// 			System.out.print("Last name : ");
	// 			String lname = sc.next();
	// 			System.out.print("Date of birth : ");
	// 			String dob = sc.next();
	// 			System.out.print("Pan number : ");
	// 			String panNo = sc.next();
	// 			registerUser(fname, mname, lname, dob, panNo);
				
				
	// 		case 2 :
	// 		// 2 - Register Company
	// 			System.out.print("Company name : ");
	// 			String companyName = sc.next();
	// 			System.out.print("Pan number : ");
	// 			String panNo2 = sc.next();
	// 			registerCompany(companyName, panNo2);
	// 			break;

	// 		case 3 :
	// 		// 3 - Analysis of particular stock
	// 			System.out.print("Stock ID : ");
	// 			int stockId3 = sc.nextInt();
	// 			analyzeStock(stockId3);
	// 			break;

	// 		case 4 :
	// 		// 4 - Buy stocks
	// 			System.out.print("Stock ID : ");
	// 			int stockId4 = sc.nextInt();
	// 			System.out.print("No. of stock units to buy : ");
	// 			int stockUnits4 = sc.nextInt();
	// 			buyStock(stockId4, stockUnits4);
	// 			break;

	// 		case 5 :
	// 		// 5 - Sell stocks
	// 			System.out.print("Stock ID : ");
	// 			int stockId5 = sc.nextInt();
	// 			System.out.print("No. of stock units to sell : ");
	// 			int stockUnits5 = sc.nextInt();
	// 			sellStock(stockId5, stockUnits5);
	// 			break;

	// 		case 6 :
	// 		// 6 - Market trends
	// 			String S="test";
	// 			getMarketTrend(S);
	// 			System.out.print("Parameter for Market trend(M/D/Y) : ");
	// 			String parTrend = sc.next();
	// 			break;

	// 		case 7 :
	// 		// 7 - User transaction history
				
	// 			getTransactionHistory(login_acc_id);
	// 			break;

	// 		//case 8 
	// 		// 8 - Index of domain wise stocks

	// 		default :
	// 			System.out.print("Wrong usecase type! \n");
	// 	}
	// 	sc.close();
		
	// }



}//end FirstExample

