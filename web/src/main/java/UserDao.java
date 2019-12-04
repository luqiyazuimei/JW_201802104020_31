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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user");
        //���������Ȼ����һ����¼����ִ��ѭ����
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            User user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
            users.add(user);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,statement,connection);
        return users;
    }
    public User find(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String findUser_id = "SELECT * FROM user where id = ?";
        //�ڸ������ϴ���Ԥ����������
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_id);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //����id����ȡ�ظ�ֵ���ʽ�����������һ����¼
        //���������һ����¼�����Ե�ǰ��¼�е�id,description,no,remarksֵΪ����������Degree����
        //���������û�м�¼���򱾷�������null
        if(resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //�ر���Դ
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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateUser_sql = "update user set username=?,password=?,teacher_id=? where id=?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        pstmt.setInt(4,user.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ���Ӽ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        return affectedRowNum > 0;
    }
    public boolean changePassword(User user) throws SQLException {
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateUser_sql = "update user set password=? where id=?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,user.getPassword());
        pstmt.setInt(2,user.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ���Ӽ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public User findByUsername(String username) throws SQLException {
        User user = null;
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username = ?");
        //ΪԤ���������ֵ
        preparedStatement.setString(1,username);
        //ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
        ResultSet resultSet = preparedStatement.executeQuery();
        //���������Ȼ����һ����¼����ִ��ѭ����
        if(resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //�ر���Դ
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
        //����sql��䣬��������Ϊռλ��
        String deleteUser_sql = "DELETE FROM user WHERE ID =?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡ���Ӽ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
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