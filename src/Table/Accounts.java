package Table;
public class Accounts {
    int account_id;
    int holder_regs_id;
	String password;
	String bank_acc_no;
	public Accounts() { }
	public Accounts (int a,int h, String p,String b){ 
		account_id = a;
        holder_regs_id=h; 
		password = p; 
        bank_acc_no = b;
		
	 }



	// GETTERS
	public int get_account_id() { return account_id;}
	public String get_bank_acc_no() { return bank_acc_no; }
	public String get_password() { return password;}
	public int get_holder_regs_id() { return holder_regs_id;}



	 // SETTERS
	public void set_bank_acc_no(String b){ 
		bank_acc_no = b;
	}
	
	public void set_password ( String p) { password = p;}
	
	public void set_account_id(int a) {account_id = a;}
	
	public void set_holder_regs_id(int h) {holder_regs_id = h;}
}
