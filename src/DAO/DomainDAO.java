package DAO;
import Tables.*;

public interface DomainDAO {
    public Domain getDomainByKey(int domain_id);
	public void addDomain(Domain domain) throws Exception;
	public void updateDomain(Domain domain) throws Exception;
	public void deleteDomain(Domain domain);
}
