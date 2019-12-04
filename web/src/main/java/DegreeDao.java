import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {

    private static DegreeDao degreeDao= new DegreeDao();

    public static DegreeDao getInstance(){
        return degreeDao;
    }

    public Collection<Degree> findAll() throws SQLException {
        //����һ������
        Set<Degree> degreeSet = new HashSet<>();
        //���һ������
        Connection connection = JdbcHelper.getConn();
        //����������
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DEGREE");
        //���������е�Ԫ�ؼ��뵽degreeSet������
        while (resultSet.next()){
            //����Degree����
            Degree degree = new Degree(resultSet.getInt("id"),
                    resultSet.getString("description"),resultSet.getString("no"),
                    resultSet.getString("remarks"));
            degreeSet.add(degree);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return degreeSet;
    }

    public Degree find(Integer id) throws SQLException{
        Degree degree = null;
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM DEGREE WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return degree;
    }
    public boolean add(Degree degree) throws SQLException{
        //������������
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO DEGREE (ID,NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?,?)");
        preparedStatement.setInt(1,degree.getId());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getDescription());
        preparedStatement.setString(4,degree.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }
    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteDegree_sql = "DELETE FROM DEGREE WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public boolean update(Degree degree) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateDepartment_sql = "UPDATE DEGREE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,degree.getDescription());
        pstmt.setString(2,degree.getNo());
        pstmt.setString(3,degree.getRemarks());
        pstmt.setInt(4,degree.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public static void main(String[] args) throws SQLException {
        DegreeDao degreeDao = new DegreeDao();
        Degree degree = degreeDao.find(3);
        System.out.println(degree);
        //ִ��Dao����ķ���
        degree.setDescription("��ʿ");
        degreeDao.update(degree);
        Degree degree1 = degreeDao.find(3);
        //��ӡ�޸ĺ󷵻صĶ���
        System.out.println(degree1);
        System.out.println("�޸�Degree����ɹ���");
    }
}

