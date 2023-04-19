package Table;
public class Company {
    int company_id;
    String company_name;

    public Company() {}
    public Company(int c,String n){
        company_id = c;
        company_name = n;
    }

    public int get_company_id() { return company_id;}
    public String get_company_name() { return company_name;}

    public void set_company_id(int c){ company_id = c;}
    public void set_company_name(String n) {company_name = n;}
}
