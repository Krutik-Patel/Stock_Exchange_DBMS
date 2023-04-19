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
	public String get_bank_acc_no() { return bank_acc_no; }
	public void set_bank_acc_no(String b){ 
		bank_acc_no = b;
	}
	public String get_password() { return password;}
	public void set_password ( String p) { password = p;}
	public int get_account_id() { return account_id;}
	public void set_account_id(int a) {account_id = a;}
	public int get_holder_regs_id() { return holder_regs_id;}
	public void get_holder_regs_id(int h) {holder_regs_id = h;}
}
