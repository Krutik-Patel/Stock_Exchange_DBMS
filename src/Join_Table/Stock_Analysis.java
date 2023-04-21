package Join_Table;
import Table.*;

public class Stock_Analysis {
    int total_units;
    float total_trans_price;
    int total_transaction;
    Stock stock;
    // Transaction trans;

    public Stock_Analysis() { }
	public Stock_Analysis (int total_units,float total_trans_price,Stock stock){ 
		this.total_units = total_units;
        this.total_trans_price = total_trans_price;
        this.stock = stock;
        
	 }


     // GETTERS
    public int get_total_units() { return total_units;}
    public int get_total_transaction() { return total_transaction;}
    public float get_total_trans_price() { return total_trans_price; }
    public Stock get_stock() { return stock; }
    // public Transaction get_transaction() { return trans; }
	

     // SETTERS
    public void set_total_units(int total_units) { this.total_units=total_units;}
    public void set_total_transaction(int total_transaction) { this.total_transaction=total_transaction;}
    public void set_total_trans_price(float total_trans_price) { 
        this.total_trans_price = total_trans_price; 
    }
    public void set_stock(Stock stock) { this.stock = stock; }
    // public void set_transaction(Transaction trans) { this.trans = trans; }

}
