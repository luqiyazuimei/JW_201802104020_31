import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DepartmentDao {
    private static DepartmentDao departmentDao=new DepartmentDao();

    public static DepartmentDao getInstance(){
        return departmentDao;
    }

    public Collection<Department> findAllBySchool(Integer school_id) throws SQLException{
        Set<Department> departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        String sql_str = "select * from Department where school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql_str);
        preparedStatement.setInt(1,school_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            Department department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    school);
            departments.add(department);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }

    public Collection<Department> findAll() throws SQLException {
        Set<Department> departments = new HashSet<Department>();
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DEPARTMENT");
        //���������Ȼ����һ����¼����ִ��ѭ����
        while (resultSet.next()){
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"),school);
            //��departemnts���������Degree����
            departments.add(department);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }

    public Department find(Integer id) throws SQLException{
        Department department = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM DEPARTMENT WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"),school);

        }
        //���������Ȼ����һ����¼����ִ��ѭ����
        JdbcHelper.close(resultSet,pstmt,connection);
        return department;
    }

    public boolean add(Department department) throws SQLException {
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO DEPARTMENT (ID,NO,DESCRIPTION,REMARKS,SCHOOL_ID) VALUES" +
                        "(?,?,?,?,?)");
        preparedStatement.setInt(1,department.getId());
        preparedStatement.setString(2,department.getNo());
        preparedStatement.setString(3,department.getDescription());
        preparedStatement.setString(4,department.getRemarks());
        preparedStatement.setInt(5,department.getSchool().getId());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteDepartment_sql = "DELETE FROM DEPARTMENT WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(Department department) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateDepartment_sql = "UPDATE DEPARTMENT SET DESCRIPTION = ?, NO = ? ,REMARKS = ?, SCHOOL_ID = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,department.getDescription());
        pstmt.setString(2,department.getNo());
        pstmt.setString(3,department.getRemarks());
        pstmt.setInt(4,department.getSchool().getId());
        pstmt.setInt(5,department.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        DepartmentDao departmentDao = new DepartmentDao();
        Department department = departmentDao.find(3);
        System.out.println(department);
        department.setDescription("��ľ");
        departmentDao.update(department);
        //��ӡ��Ӻ󷵻صĶ���
        Department department2 = departmentDao.find(3);
        System.out.println(department2);
        System.out.println("�޸�Department����ɹ���");
    }
}

