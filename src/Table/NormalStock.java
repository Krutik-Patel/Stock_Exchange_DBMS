
package Table;

public class NormalStock {
    int normal_stk_id;
    int stk_comp_id;
    boolean in_index;

    public NormalStock() {}
    public NormalStock(int norm_stk_id, int stck_comp_id, boolean in_ind) {
        normal_stk_id = norm_stk_id;
        stk_comp_id = stck_comp_id;
        in_index = in_ind;
    }

    public int get_normal_stk_id() { return normal_stk_id; }
    public int get_stk_comp_id() { return stk_comp_id;}
    public boolean get_in_index() { return in_index;}


    public void set_normal_stk_id(int n) {  normal_stk_id=n; }
    public void set_stk_comp_id(int s) {  stk_comp_id=s;}
    public void set_in_index(boolean i) {  in_index=i;}
}
