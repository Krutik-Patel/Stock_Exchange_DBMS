package DAO;
import Tables.*;

public interface CompanyDAO {
    public Company getCompanyByKey(int company_id);
	public void addCompany(Company company) throws Exception;
	public void updateCompany(Company company) throws Exception;
	public void deleteCompany(Company company);
}
