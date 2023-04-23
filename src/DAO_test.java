//STEP 1. Import required packages
import DAO.*;

import Table.*;
import Join_Table.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DAO_test {
	public static DAO_Factory daoFactory;
	public static void main(String[] args) throws Exception {
		try{
			daoFactory = new DAO_Factory();

			// System.out.println("Running Transaction History");
			// usecase_stock_analysis();
			// System.out.println("......");
			// // usecase_register_user();
			// // System.out.println("......");
			// // usecase_display();
			// // System.out.println();
			// // usecase_delete();

			System.out.println("Market trend printed...");
			usecase_market_trend();

		}catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
		}
	}
	//end main
	public static void usecase_register_user() throws Exception
	{
		Participants s1 = new Participants();
		User u1 = new User();
		
		// Participants s2 = new Participants();
		

		// User def

		String d = "13/08/2023";
		Date date =new SimpleDateFormat("dd/MM/yyyy").parse(d);

		String d1 = "24/05/2001";
		Date dob =new SimpleDateFormat("dd/MM/yyyy").parse(d1);

		s1.set_regs_id(10);
		s1.set_regs_date(date);
		s1.set_pan_no("99988990");

		u1.set_user_id(10);
		u1.set_fname("Why");
		u1.set_mname("are");
		u1.set_lname("you");
		u1.set_dob(dob);

		try{
			// Start transaction boundary
			daoFactory.activateConnection();

			// Carry out DB operations using DAO
			ParticipantDAO sdao = daoFactory.getParticipantDAO();
			UserDAO udao = daoFactory.getUserDAO();
			sdao.addParticipant(s1);;
			udao.addUser(u1);
			// End transaction boundary with success
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				// End transaction boundary with failure
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}



	// Stock Analysis
	public static void usecase_stock_analysis() throws Exception
	{
		try{
			

			daoFactory.activateConnection();
			// ParticipantDAO pdao = daoFactory.getParticipantDAO();
			StockDAO sdao = daoFactory.getStockDAO();

			int[] stk_id={2,1};
			String date = "2023-01-23";

			for (int id : stk_id){
				System.out.println("\n> Trying stock_id =" + id);
				Stock_Analysis s1 = sdao.get_stock_analysis(id,date);
				
				if(s1 != null){
				Stock st = s1.get_stock();
				System.out.println("\n Stock Name: "+st.get_stock_name()+" -- Stock Units: "+st.get_stock_units()+" -- Stock Price: "+st.get_stock_price()+" -- Stock Domain ID: "+st.get_dom_id());
				System.out.println(" Transacted money from "+date+": "+s1.get_total_trans_price()+" -- Transacted units in past month: "+s1.get_total_units()+"\n -- Total number of transactions in past month: "+s1.get_total_transaction());
				}
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}


	// Transaction History
	public static void usecase_transaction_history() throws Exception
	{
		try{
			

			daoFactory.activateConnection();
			// ParticipantDAO pdao = daoFactory.getParticipantDAO();
			TransactionDAO sdao = daoFactory.getTransactionDAO();

			int[] account_id={2,1,5,6};

			for (int id : account_id){
				System.out.println("\n> Trying account_id =" + id);
				ArrayList<Transaction_History> th = sdao.getTransactionHistory(id);
				for(Transaction_History element : th) {
					Transaction t = element.get_trans();

					if(th != null){
				System.out.println("\n Transaction ID: "+t.get_trans_id()+" -- Transaction Date: "+t.get_trans_date()+" -- Units: "+t.get_units()+" -- Price: "+t.get_trans_price()+" -- FROM: "+element.get_from_name()+" -- TO: "+element.get_to_name()+ " -- Stock Name: "+element.get_stock_name());

				System.out.println("++++++++++++++++++++++++++++++++++++++++++");
				
				}
				
				}
				
				
			}

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}
	
	





	public static void usecase_delete()
	{
		Participants s1 = new Participants();
		Participants s2 = new Participants();
		

		
		s1.set_regs_id(10);
		s2.set_regs_id(11);
		

		try{
			daoFactory.activateConnection();
			ParticipantDAO sdao = daoFactory.getParticipantDAO();
			
			sdao.deleteParticipant(s1);
			sdao.deleteParticipant(s2);
			
			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}


	public static void usecase_display()
	{
		try{
			int id;

			daoFactory.activateConnection();
			ParticipantDAO pdao = daoFactory.getParticipantDAO();
			StockDAO sdao = daoFactory.getStockDAO();

			id=2;
			System.out.println("Trying id=" + id);
			Stock s1 = sdao.getStockByKey(id);
			if(s1 != null)
			System.out.println("Stock Name: "+s1.get_stock_name());

			id=9;
			System.out.println("Trying id=" + id);
			Participants s2 = pdao.getParticipantByKey(id);
			if(s2 != null)
			s2.print();

			id=10;
			System.out.println("Trying id=" + id);
			Participants s3 = pdao.getParticipantByKey(id);

			if(s3 != null)
			s3.print();

			daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.COMMIT );
		}catch(Exception e){
				daoFactory.deactivateConnection( DAO_Factory.TXN_STATUS.ROLLBACK );
				e.printStackTrace();
		}
	}

	public static void usecase_market_trend() throws Exception
	{
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
	
}//end FirstExample
