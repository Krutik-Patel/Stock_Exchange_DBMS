package Table;
public class Domain {
    int domain_id;
    String domain_name;

    public Domain() {}
    public Domain(int dom_id,String dom_name){
        domain_id = dom_id;
        domain_name = dom_name;
    }

    public int get_Domain_id() { return domain_id;}
    public String get_Domain_name() { return domain_name;}

    public void set_Domain_id(int d){ domain_id = d;}
    public void set_Domain_name(String n) {domain_name = n;}
}
