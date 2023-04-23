
import DAO.*;
import Table.*;
import Join_Table.*;
import java.util.Date;
import java.util.Scanner;

import java.text.SimpleDateFormat;

import java.util.ArrayList;


public class DAO_Demo {
	private static DAO_Factory daoFactory;
	
	public static void main(String[] args) throws Exception {
		try{

			daoFactory = new DAO_Factory();
			inputHandler();

		}catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
		}
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
	public static int registerUser(String fname, String mname, String lname, String dob, String panNo) throws Exception {
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
		} catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}

		return participantNum;
	}
	

	public static int registerCompany(String companyName, String panNo){
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
		} catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}

		return participantNum;
	}


	// ACCOUNT LOGIN and REGISTRATION
	public static int registerAccount(String password, int holder_regs_id,String bank_acc_no){

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

		return accountNum;
	}

	public static boolean loginAccount(int account_id,String password){
		

		try {
			// Start transaction boundary
			daoFactory.activateConnection();

			// Insert participant
			AccountsDAO sdao = daoFactory.getAccountsDAO();

			Accounts loginAcc = sdao.getAccountByKey(account_id);

			if(loginAcc.get_password().equals(password)){
				// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
				return true;
			}
			else return false;
			

			
		} catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
				return false;
		}

		


	}

	// ------------------------

	
	public static void analyzeStock(int stockId){
		
	}

	public static void buyStock(int stockId, int stockUnits){
	}

	public static void sellStock(int stockId, int stockUnits){
		
	}

	public static void getMarketTrend(String par){
		try{
		
			daoFactory.activateConnection();
			
			StockDAO sdao = daoFactory.getStockDAO();
			
			ArrayList<Market_Trend> mark_trend = sdao.get_market_trend("2020-08-01", "2024-12-01");
			
			for (Market_Trend t : mark_trend) {
				System.out.println("Stock Name:" + t.get_stock_name());	
				System.out.println("Stock ID:" + t.get_stock_id());	
				System.out.println("Prev price:" + t.get_prev_price());	
				System.out.println("Curr price:" + t.get_curr_price());	
			}
			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );

		} catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}
	
	public static void getTransactionHistory(int account_id){


			try{
			

			daoFactory.activateConnection();
			// ParticipantDAO pdao = daoFactory.getParticipantDAO();
			TransactionDAO sdao = daoFactory.getTransactionDAO();

			
				for(int i=0;i< 40;i++) System.out.print("-");
				ArrayList<Transaction_History> th = sdao.getTransactionHistory(account_id);
				for(Transaction_History element : th) {
					Transaction t = element.get_trans();

					if(th != null){
				System.out.println("\n<> -- Transaction ID: "+t.get_trans_id()+" -- Transaction Date: "+t.get_trans_date()+"\n<> -- Units: "+t.get_units()+" -- Price: "+t.get_trans_price()+"\n<> -- FROM: "+element.get_from_name()+" -- TO: "+element.get_to_name()+ " -- Stock Name: "+element.get_stock_name());

				
				
				for(int i=0;i< 40;i++) System.out.print("-");
				}
				
			}
			System.out.println();

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}

		
	}

	//Input handler
	public static void inputHandler() throws Exception {

		int login_acc_id = -1;



		//System.out.println();
		Scanner sc = new Scanner(System.in);

		while(true){

			System.out.print("Already have an account (y / n): ");
			String register = sc.next();

			if(register.equals("y")){
				System.out.print("Account ID : ");
				int acc_id = sc.nextInt();
				System.out.print("Password :");
				String pass = sc.next();
				
				if( loginAccount(acc_id,pass)){
					login_acc_id = acc_id;
					break;
				}
				else{
					System.out.println(" -- Wrong ID or PASSWORD --");
					continue;
				} 
					
			}
			else{

				int holder_regs_id;
				System.out.print("Register User (u) / Company (c): ");
				String part_choice = sc.next();
				if(part_choice.equals("u")){
						// 1 - Register User
					System.out.print("First name : ");
					String fname = sc.next();
					System.out.print("Middle name : ");
					String mname = sc.next();
					System.out.print("Last name : ");
					String lname = sc.next();
					System.out.print("Date of birth : ");
					String dob = sc.next();
					System.out.print("Pan number : ");
					String panNo = sc.next();
					holder_regs_id = registerUser(fname, mname, lname, dob, panNo);
				
				}
				else if(part_choice.equals("c")){
						// 2 - Register Company
					System.out.print("Company name : ");
					String companyName = sc.next();
					System.out.print("Pan number : ");
					String panNo2 = sc.next();
					holder_regs_id = registerCompany(companyName, panNo2);
				
				}

				else continue;

				System.out.println(" -- SignUP Account --");
				System.out.print("SET Password: ");
				String password = sc.next();
				System.out.print("Bank Account no.: ");
				String bank_acc_no = sc.next();
				int acc_id = registerAccount(password, holder_regs_id, bank_acc_no);

				System.out.println(" -- Your Account ID: "+acc_id+" --");
				if( loginAccount(acc_id,password)){
					login_acc_id = acc_id;
					break;
				}
				else{
					System.out.println(" -- Wrong ID or PASSWORD --");
					continue;
				} 
			}

			

			
		}


		System.out.print("Usecase Type : ");

		int usecaseType = sc.nextInt();
		switch(usecaseType){
			case 1 :
			// 1 - Register User
				System.out.print("First name : ");
				String fname = sc.next();
				System.out.print("Middle name : ");
				String mname = sc.next();
				System.out.print("Last name : ");
				String lname = sc.next();
				System.out.print("Date of birth : ");
				String dob = sc.next();
				System.out.print("Pan number : ");
				String panNo = sc.next();
				registerUser(fname, mname, lname, dob, panNo);
				break;
				
			case 2 :
			// 2 - Register Company
				System.out.print("Company name : ");
				String companyName = sc.next();
				System.out.print("Pan number : ");
				String panNo2 = sc.next();
				registerCompany(companyName, panNo2);
				break;

			case 3 :
			// 3 - Analysis of particular stock
				System.out.print("Stock ID : ");
				int stockId3 = sc.nextInt();
				analyzeStock(stockId3);
				break;

			case 4 :
			// 4 - Buy stocks
				System.out.print("Stock ID : ");
				int stockId4 = sc.nextInt();
				System.out.print("No. of stock units to buy : ");
				int stockUnits4 = sc.nextInt();
				buyStock(stockId4, stockUnits4);
				break;

			case 5 :
			// 5 - Sell stocks
				System.out.print("Stock ID : ");
				int stockId5 = sc.nextInt();
				System.out.print("No. of stock units to sell : ");
				int stockUnits5 = sc.nextInt();
				sellStock(stockId5, stockUnits5);
				break;

			case 6 :
			// 6 - Market trends
				String S="test";
				getMarketTrend(S);
				System.out.print("Parameter for Market trend(M/D/Y) : ");
				String parTrend = sc.next();
				break;

			case 7 :
			// 7 - User transaction history
				System.out.print("Account ID : ");
				int account_id = sc.nextInt();
				getTransactionHistory(account_id);
				break;

			//case 8 
			// 8 - Index of domain wise stocks

			default :
				System.out.print("Wrong usecase type! \n");
		}
		sc.close();
		
	}



}//end FirstExample

