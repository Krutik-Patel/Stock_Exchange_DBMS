package Table;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Participants{
	int regs_id;
	String pan_no;
	String regs_date;
	public Participants() { }
	public Participants (int r, String p,Date d){ 
		regs_id = r; 
		pan_no = p; 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate= formatter.format(d); 
		regs_date = strDate;
	 }
	public String get_regs_date() { return regs_date; }
	public void set_regs_date(Date d){ 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate= formatter.format(d);  
		regs_date = strDate;
	}
	public String get_pan_no() { return pan_no;}
	public void set_pan_no ( String p) { pan_no = p;}
	public int get_regs_id() { return regs_id;}
	public void set_regs_id(int r) {regs_id = r;}
	public void print(){ 
		System.out.println("Registration ID " + regs_id +" : Registration Date " + regs_date + " : Pan-Card no. " + pan_no);
	}
};

