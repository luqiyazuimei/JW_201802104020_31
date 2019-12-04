import java.sql.*;
import java.util.*;
import java.util.Date;


public class UserDao {
    private static UserDao userDao=new UserDao();
    private UserDao(){}
    public static UserDao getInstance(){
        return userDao;
    }

    public Set<User> findAll() throws SQLException {
        Set<User> users = new HashSet<User>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            User user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
            users.add(user);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return users;
    }
    public User find(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String findUser_id = "SELECT * FROM user where id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_id);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if(resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public boolean add(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("insert into user (username, password,teacher_id) values (?,?,?)");
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        int affectedRowNum  = pstmt.executeUpdate();
        return affectedRowNum>0;
    }
    public boolean add(Connection connection,User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("insert into user (username, password,teacher_id) values (?,?,?)");
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        int affectedRowNum  = pstmt.executeUpdate();
        return affectedRowNum>0;

    }
    public boolean update(User user) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateUser_sql = "update user set username=?,password=?,teacher_id=? where id=?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        pstmt.setInt(4,user.getId());
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        return affectedRowNum > 0;
    }
    public boolean changePassword(User user) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateUser_sql = "update user set password=? where id=?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,user.getPassword());
        pstmt.setInt(2,user.getId());
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public User findByUsername(String username) throws SQLException {
        User user = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username = ?");
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        if(resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return user;
    }
    public static boolean addUser(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("insert into user(username,password,teacher_id) values " +
                "(?,?,?)");
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setInt(3, user.getTeacher().getId());
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public boolean delete(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteUser_sql = "DELETE FROM user WHERE ID =?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 条");
        return affectedRowNum > 0;
    }
    public User userLogin(String username,String password) throws SQLException{
        User user = null;
        User userToLogin = userDao.findByUsername(username);
        if(userToLogin.getPassword().equals(password)){
            user = userToLogin;
        }
        return user;
    }
}