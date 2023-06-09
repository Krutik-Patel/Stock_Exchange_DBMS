
package Table;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction {
    int trans_id;
    int acc_id_from;
    int acc_id_to;
    int stk_id;
    int units;
    String trans_date;
    float trans_price;

    public Transaction() {}
    public Transaction(int trans_id, int acc_id_from,int acc_id_to,int stk_id,int units,Date trans_date,float trans_price){
        
        this.trans_id = trans_id;
        this.acc_id_from = acc_id_from;
        this.acc_id_to = acc_id_to;
        this.stk_id = stk_id;
        this.units =units;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate= formatter.format(trans_date); 
		this.trans_date = strDate;

        this.trans_price = trans_price;
    }

    public int get_trans_id() { return trans_id;}
    public int get_acc_id_from() { return acc_id_from;}
    public int get_acc_id_to() { return acc_id_to;}
    public int get_stk_id() { return stk_id;}
    public int get_units() { return units;}
    public String get_trans_date() { return trans_date;}
    public float get_trans_price() { return trans_price;}


    public void set_trans_id(int t) {  trans_id=t;}
    public void set_acc_id_from(int af) {  acc_id_from=af;}
    public void set_acc_id_to(int at) {  acc_id_to=at;}
    public void set_stk_id(int s) {  stk_id=s;}
    public void set_units(int u) {  units=u;}
    public void set_trans_date(Date d) {  
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		String strDate= formatter.format(d); 
		trans_date = strDate;
        }
    public void set_trans_price(float p) { trans_price=p;}

    


}
