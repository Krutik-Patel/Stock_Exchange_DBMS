
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
      
      ServerSocket server=new ServerSocket(8888);
      int counter=0;
      System.out.println("Server Started ....");
      while(true){
        
        counter++;
        Socket serverClient=server.accept();  //server accept the client connection request
        System.out.println(" >> " + "Client No:" + counter + " started!");
        ServerClientThread sct = new ServerClientThread(serverClient,counter); //send  the request to a separate thread
        sct.start();
      }
      
    }catch(Exception e){
      System.out.println(e);
    }
  }
}

class ServerClientThread extends Thread {
  public static DAO_Factory daoFactory;
  Socket serverClient;
    int clientNo;
    int squre;
   
    ServerClientThread(Socket inSocket, int counter){
      serverClient = inSocket;
      clientNo=counter;
    }

   



    public void run(){
      
      try{
        daoFactory = new DAO_Factory();
        DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
        DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
        String clientMessage="", serverMessage="";
        while(!clientMessage.equals("bye")){
          
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
      }catch(Exception ex){
        System.out.println(ex);
      }finally{
        System.out.println("Client -" + clientNo + " exit!! ");
      }
    }

    // Util Functions


    public static String parseResponse(String cliRes) throws Exception{
		try{
			String[] resp = cliRes.split(" ");
            
           
      if(resp[0].equals("1")){
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
		catch(Exception e){
			return "error";
		}
		return "No-result";
	}


  // DB Functions


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
			
			ArrayList<Market_Trend> mark_trend = sdao.get_market_trend("2020-08-01", "2024-12-01");
			
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


  public static String buyStock(int accountId,int  stockId,int stockUnits) throws Exception{
     return "";
  }

  public static String sellStock(int accountId,int  stockId,int stockUnits) throws Exception{
     return "";
  }


  }