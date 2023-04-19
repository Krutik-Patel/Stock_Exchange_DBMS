package Table;
public class Stock {
    int stock_id;
    String stock_name;
    int stock_units;
    float stock_price;
    int dom_id;

    public Stock() {}
    public Stock(int s,String n,int u,float p,int d){
        stock_id = s;
        stock_name = n;
        stock_units = u;
        stock_price = p;
        dom_id = d;
    }

    public int get_stock_id(){ return stock_id;}
    public String get_stock_name() { return stock_name; }
    public int get_stock_units() { return stock_units; }
    public float get_stock_price() { return stock_price; }
    public int get_dom_id() { return dom_id; }

    public void set_stock_id(int s) { stock_id = s;}
    public void set_stock_name(String n) { stock_name = n; }
    public void set_stock_units(int u) { stock_units = u;}
    public void set_stock_price(float p ) { stock_price = p;}
    public void set_dom_id(int d) { dom_id =d;}

}
