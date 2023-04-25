
package Table;

public class IndexStock {
    int index_stk_id;
    int stock_quant;

    public IndexStock() {}
    public IndexStock(int ind_stk_id,int stk_qty) {
        index_stk_id = ind_stk_id;
        stock_quant = stk_qty;
    }

    public int get_index_stk_id() { return index_stk_id; }
    public int get_stock_quant() { return stock_quant;}
    


    public void set_index_stk_id(int i) {  index_stk_id=i; }
    public void set_stock_quant(int s) {  stock_quant=s;}
}
