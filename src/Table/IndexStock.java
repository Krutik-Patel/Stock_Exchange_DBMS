package Table;
public class IndexStock {
    int index_stk_id;
    int stock_quant;

    public IndexStock() {}
    public IndexStock(int i,int s) {
        index_stk_id = i;
        stock_quant = s;
    }

    public int get_index_stk_id() { return index_stk_id; }
    public int get_stock_quant() { return stock_quant;}
    


    public void set_index_stk_id(int i) {  index_stk_id=i; }
    public void set_stock_quant(int s) {  stock_quant=s;}
}
