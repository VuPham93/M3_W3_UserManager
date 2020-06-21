package services;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserManager {
    public void insertUser(User user) throws SQLException;

    public User selectUser(int id);

    public List<User> selectAllUsers();

    public boolean deleteUser(int id) throws SQLException;

    public boolean updateUser(User user) throws SQLException;

    public List<User> findUser(String country);

    public List<User> sortUserByName();

    public User getUserById(int id);

    public void insertUserStore(User user) throws SQLException;

    public void addUserTransaction(User user, int[] permission);

    public void insertUpdateWithoutTransaction();

    public void insertUpdateUseTransaction();

    public List<User> selectAllUserStoreProcedure();

    public boolean updateUserStoreProcedure(User user) throws SQLException;

    public boolean deleteUserStoreProcedure(int id) throws SQLException;

}
