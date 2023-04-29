package Join_Table;

public class Index_Info {
    private int index_stk_id;
    private String stock_name;
    private int stock_units;
    private float stock_price;
    private int dom_id;
    private String domain_name;
    private int stock_quant;

    public Index_Info() {}
    public Index_Info( int index_stk_id, String stock_name, int stock_units, float stock_price, int dom_id, String domain_name, int stock_quant){
        this.index_stk_id = index_stk_id;
        this.stock_name = stock_name;
        this.stock_units = stock_units ;
        this.stock_price = stock_price;
        this.dom_id = dom_id;
        this.domain_name = domain_name;
        this.stock_quant = stock_quant;
    }

    //getters
    public int get_index_stk_id(){
        return index_stk_id;
    }
    public String get_stock_name(){
        return stock_name;
    }
    public int get_stock_units(){
        return stock_units;
    }
    public float get_stock_price(){
        return stock_price;
    }
    public int get_dom_id(){
        return dom_id;
    }
    public String get_domain_name(){
        return domain_name;
    }
    public int get_stock_quant(){
        return stock_quant;
    }

    //setters
    public void set_index_stk_id(int index_stk_id){
        this.index_stk_id = index_stk_id;
    }
    public void set_stock_name(String stock_name){
        this.stock_name = stock_name;
    }
    public void set_stock_units(int stock_units){
        this.stock_units = stock_units;
    }
    public void set_stock_price(float stock_price){
        this.stock_price = stock_price;
    }
    public void set_dom_id(int dom_id){
        this.dom_id = dom_id;
    }
    public void set_domain_name(String domain_name){
        this.domain_name = domain_name;
    }
    public void set_stock_quant(int stock_quant){
        this.stock_quant = stock_quant;
    }
}