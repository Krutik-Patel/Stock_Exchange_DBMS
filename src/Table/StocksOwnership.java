
package Table;

public class StocksOwnership {
    int stck_id;
    int owner_id;
    int units_owned;

    public StocksOwnership() {}
    public StocksOwnership(int stck_id,int owner_id,int units_owned) {
        this.stck_id = stck_id;
        this.owner_id= owner_id;
        this.units_owned = units_owned;
    }

    public int get_stck_id() { return stck_id; }
    public int get_owner_id() { return owner_id;}
    public int get_units_owned() { return units_owned;}
    


    public void set_stck_id(int s) {  stck_id=s; }
    public void set_owner_id(int o) {  owner_id=o;}
    public void set_units_owned(int u) {  units_owned=u;}
}
