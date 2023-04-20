//STEP 1. Import required packages
import DAO.*;

import Table.*;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DAO_Demo {
	public static DAO_Factory daoFactory;
	public static void main(String[] args) throws Exception {
		try{
			daoFactory = new DAO_Factory();

			System.out.println("Running Registration");
			usecase_display();
			System.out.println("......");
			usecase_register_user();
			System.out.println("......");
			usecase_display();
			System.out.println();

			

			// System.out.println("Running usecase_delete");
			// usecase_delete();
			// System.out.println("......");
			// usecase_display();
			// System.out.println();

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

		


		// Company def

		// String d2 = "13/08/2026";
		// Date date1 =new SimpleDateFormat("dd/MM/yyyy").parse(d2);

		// s2.set_regs_id(9);
		// s2.set_regs_date(date1);
		// s2.set_pan_no("6668887");

		

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
}//end FirstExample
