package Table;

public class Company {
    int company_id;
    String company_name;

    public Company() {
    }

    public Company(int compID, String compName) {
        company_id = compID;
        company_name = compName;
    }

    public int get_company_id() {
        return company_id;
    }

    public String get_company_name() {
        return company_name;
    }

    public void set_company_id(int compID) {
        company_id = compID;
    }

    public void set_company_name(String compName) {
        company_name = compName;
    }

}
