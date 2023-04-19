package DAO_JDBC;
import DAO.ParticipantDAO;
import Tables.*;
import java.util.Date;
import java.sql.*;

import java.text.SimpleDateFormat;



public class ParticipantDAO_JDBC implements ParticipantDAO {
	
	Connection dbConnection;

	public ParticipantDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

	@Override
	public Participants getParticipantByKey(int regis_id) {
		Participants p = new Participants();
		String sql;
		Statement stmt = null;
		boolean flag = false;
		
		try{
			stmt = dbConnection.createStatement();
			sql = "select regs_id,regs_date,pan_no from Participants where regs_id = " + regis_id;
			ResultSet rs = stmt.executeQuery(sql);

			
																																																																																																																																																																																			
			//STEP 5: Extract data from result set
			
			while(rs.next()){
				//Retrieve by column name
				flag=true;
				int regs_id  = rs.getInt("regs_id");
				Date regs_date = rs.getDate("regs_date");
				String pan_no = rs.getString("pan_no");
				p.set_regs_id(regs_id);
				p.set_regs_date(regs_date);
				p.set_pan_no(pan_no);
				break;
				// Add exception handling here if more than one row is returned
			}
			if(rs.next()){
				System.out.println("More than one record found for the given regs_id");
				return null;
			}
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		// Add exception handling when there is no matching record
		if(!flag){
			System.out.println("No Record Found");
			return null;
		}

		
		return p;
	}


	@Override
	public void addParticipant(Participants participant) throws Exception{
		PreparedStatement preparedStatement = null;																																																																																																																																													
		String sql;
		sql = "insert into Participants(regs_id,regs_date,pan_no) values (?,?,?)";

		
		
		
		String d = participant.get_regs_date();
		Date date =new SimpleDateFormat("dd/MM/yyyy").parse(d);
		
		

		try {
			preparedStatement = dbConnection.prepareStatement(sql);
 
			preparedStatement.setInt(1, participant.get_regs_id());
			
			preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
			preparedStatement.setString(3, participant.get_pan_no());
 
			// execute insert SQL stetement
			preparedStatement.executeUpdate();
 
			System.out.println("Participant with ID " + participant.get_regs_id() 
				+ ", added to the database");
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}

		try{
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}

		
	}


	@Override
	public void updateParticipant(Participants participant) throws Exception{

	
		
		PreparedStatement preparedStatement = null;																																																																																																																																													
		String sql;
		sql = "update Participants set regs_date=?,pan_no=? where regs_id = "+ participant.get_regs_id();
		try {
			preparedStatement = dbConnection.prepareStatement(sql);
 
			String d = participant.get_regs_date();
			Date date =new SimpleDateFormat("dd/MM/yyyy").parse(d);

			preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
			preparedStatement.setString(2, participant.get_pan_no());
 
			// execute update SQL stetement
			int ret = preparedStatement.executeUpdate();

			if(ret != 0) {
				
				System.out.println("Participant with regs_id: " + participant.get_regs_id() 
				+ ", updated successfully");
				}
				else {
					System.out.println("Update Failed for regs_id: "+participant.get_regs_id());
				}
 
			
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}

		try{
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}
	}

	@Override
	public void deleteParticipant(Participants participant){
		 // Deletion
		PreparedStatement preparedStatement = null;																																																																																																																																													
		String sql;
		sql = "DELETE FROM Participants where regs_id = ?;";
		try {
			preparedStatement = dbConnection.prepareStatement(sql);
 
			preparedStatement.setInt(1, participant.get_regs_id());
 
			// execute delete SQL stetement
			int ret = preparedStatement.executeUpdate();

			
 
			if(ret != 0) {
			System.out.println("Participant with id " + participant.get_regs_id() 
				+ " deleted from the database");
			}
			else {
				System.out.println("Delete Failed for id: "+participant.get_regs_id());
			}

		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}

		try{
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		}
	}



	




	

	
}
