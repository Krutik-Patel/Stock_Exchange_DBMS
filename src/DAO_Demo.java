import DAO.*;
import Table.*;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class DAO_Demo {
	private static DAO_Factory daoFactory;
	private static int participantNum;
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
			int generatedID = daoFactory.getParticipantDAO().getTotalParticipants() + 1;			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
			return generatedID;
		} catch (Exception e) {
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
			e.printStackTrace();
			return -1;
		}
	}
	//end main
	public static void registerUser(String fname, String mname, String lname, String dob, String panNo) throws Exception {
		Date currDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		Date dateOfBirth = sdf.parse(dob);
		participantNum = generateParticipantID();
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
	}
	
	public static void registerCompany(String companyName, String panNo){
		Date currDate = new Date();
		participantNum = generateParticipantID();
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
	}
	
	public static void analyzeStock(int stockId){
	}

	public static void buyStock(int stockId, int stockUnits){
	}

	public static void sellStock(int stockId, int stockUnits){
	}

	public static void getMarketTrend(){
	}
	
	public static void getTransactionHistory(int regsId){
	}

	//Input handler
	public static void inputHandler() throws Exception {
		//System.out.println();
		Scanner sc = new Scanner(System.in);
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
				getMarketTrend();
				break;

			case 7 :
			// 7 - User transaction history
				System.out.print("Registration ID : ");
				int regsId = sc.nextInt();
				getTransactionHistory(regsId);
				break;

			//case 8 
			// 8 - Index of domain wise stocks

			default :
				System.out.print("Wrong usecase type! \n");
		}
		sc.close();
		
	}



}//end FirstExample
