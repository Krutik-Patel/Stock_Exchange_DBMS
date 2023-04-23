package Join_Table;

public class Market_Trend {
    String stock_name;
    int stock_id; 
    float prev_price, curr_price;

    public Market_Trend() {};

    public Market_Trend(String stock_name, int stock_id, int prev_price, int curr_price) {
        this.stock_name = stock_name;
        this.stock_id = stock_id;
        this.prev_price = prev_price;
        this.curr_price = curr_price;
    }
    
    // GETTERS
    public String get_stock_name() { return stock_name;}
    public int get_stock_id() { return stock_id;}
    public float get_prev_price() { return prev_price; }
    public float get_curr_price() { return curr_price; }

    // SETTERS
    public void set_stock_name(String stock_name) { this.stock_name = stock_name;}
    public void set_stock_id(int stock_id) { this.stock_id = stock_id; }
    public void set_prev_price(float prev_price) { this.prev_price = prev_price; }
    public void set_curr_price(float curr_price) { this.curr_price = curr_price; }

}


