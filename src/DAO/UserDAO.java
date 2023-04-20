package DAO;

import Table.*;

public interface UserDAO {
	public User getUserByKey(int user_id);

	public void addUser(User user) throws Exception;

	public void updateUser(User user) throws Exception;

	public void deleteUser(User user);
}
