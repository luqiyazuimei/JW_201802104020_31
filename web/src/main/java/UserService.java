import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;


public class UserService {
    private static UserDao userDao= UserDao.getInstance();
    private static UserService userService=new UserService();
    private UserService(){}

    public static UserService getInstance(){
        return userService;
    }

    public Collection<User> findAll() throws SQLException {
        return userDao.findAll();
    }

    public boolean add(Connection connection, User user) throws SQLException {
        return userDao.add(connection,user);
    }
    public User find(Integer id) throws SQLException {
        return userDao.find(id);
    }

    public boolean update(User user) throws SQLException {
        return userDao.update(user);
    }

    public boolean addUser(User user) throws SQLException {
        return userDao.addUser(user);
    }

    public boolean delete(Integer id) throws SQLException {
        return userDao.delete(id);
    }

    public boolean changePassword(User user) throws SQLException{
        return userDao.changePassword(user);
    }

    public User userLogin(String username,String password) throws SQLException {
        return userDao.userLogin(username,password);
    }
}
