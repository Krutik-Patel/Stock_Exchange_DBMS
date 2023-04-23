package Join_Table;

import Table.*;

public class Transaction_History {
    String from_name, to_name;
    String stock_name;
    Transaction trans;


    public Transaction_History() { }
	public Transaction_History (String from_name,String to_name,Transaction trans){ 
		this.from_name = from_name;
        this.to_name = to_name;
        this.trans = trans;
        
	 }


     // GETTERS

    public String get_from_name() { return from_name;}
    public String get_to_name() { return to_name;}
    public String get_stock_name() { return stock_name;}
    public Transaction get_trans() { return trans;}
    
   
	

     // SETTERS
    public void set_from_name(String from_name) { this.from_name = from_name;}
    public void set_to_name(String to_name) { this.to_name  = to_name;}
    public void set_stock_name(String stock_name) { this.stock_name  = stock_name;}
    public void set_trans(Transaction trans) { this.trans = trans;}
    
}
