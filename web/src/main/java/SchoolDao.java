import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SchoolDao {
    private static SchoolDao schoolDao = new SchoolDao();

    public static SchoolDao getInstance(){
        return schoolDao;
    }

    public Collection<School> findAll() throws SQLException {
        Set<School> schools = new HashSet<School>();
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SCHOOL");
        //���������Ȼ����һ����¼����ִ��ѭ����
        while (resultSet.next()){
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            School school = new School(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
            //��degrees���������Degree����
            schools.add(school);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,statement,connection);
        return schools;
    }

    public School find(Integer id) throws SQLException{
        School school = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM SCHOOL WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            school = new School(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return school;
    }

    public boolean add (School school) throws SQLException{
        //������������
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO SCHOOL (ID,NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?,?)");
        preparedStatement.setInt(1,school.getId());
        preparedStatement.setString(2,school.getNo());
        preparedStatement.setString(3,school.getDescription());
        preparedStatement.setString(4,school.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteSchool_sql = "DELETE FROM SCHOOL WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(School school) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateSchool_sql = "UPDATE SCHOOL SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateSchool_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,school.getDescription());
        pstmt.setString(2,school.getNo());
        pstmt.setString(3,school.getRemarks());
        pstmt.setInt(4,school.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        SchoolDao schoolDao = new SchoolDao();
        School school = schoolDao.find(972);
        System.out.println(school);
        school.setDescription("����ѧԺ");
        schoolDao.update(school);
        School school1 = schoolDao.find(972);
        //��ӡ�޸ĺ󷵻صĶ���
        System.out.println(school);
        System.out.println("�޸�School����ɹ���");
    }
}
