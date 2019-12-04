import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		//������Ӷ���
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TEACHER");
		//���������Ȼ����һ����¼����ִ��ѭ����
		while(resultSet.next()) {
			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
			Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
					profTitle, degree, department,resultSet.getString("no"));
			teachers.add(teacher);
		}
		//�ر���Դ
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}
    public Teacher find(Integer id) throws SQLException{
	    Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM TEACHER WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
            Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),
                    profTitle,degree,department,resultSet.getString("no"));
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,pstmt,connection);
        return teacher;
    }
	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//����sql��䣬��������Ϊռλ��
		String updateTeacher_sql = "UPDATE TEACHER SET NO=?, NAME = ?, PROFTITLE_ID = ?, DEGREE_ID = ?, DEPARTMENT_ID = ? WHERE ID = ?";
		//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
		PreparedStatement pstmt = connection.prepareStatement(updateTeacher_sql);
		//ΪԤ�������������ֵ
        pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		pstmt.setString(5,teacher.getNo());
		pstmt.setInt(6,teacher.getId());
		//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("�޸��� "+affectedRowNum+" ��");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean add(Teacher teacher){
		Connection connection = null;
		PreparedStatement pstmt = null;
		int affectedRowNum = 0;
		try {
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			//��ӽ�ʦ����
			String addTeacher_sql = "insert into teacher (id,name,proftitle_id, degree_id, department_id,no)" +
					" values (?,?,?,?,?,?)";
			pstmt = connection.prepareStatement(addTeacher_sql);
			pstmt.setInt(1,teacher.getId());
			pstmt.setString(2,teacher.getName());
			pstmt.setInt(3,teacher.getTitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());
			pstmt.setString(6,teacher.getNo());
			pstmt.executeUpdate();
			pstmt = connection.prepareStatement("select * from teacher where id=(select max(id) from teacher)");
			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			int teacherID=resultSet.getInt("id");
			teacher.setId(teacherID);
			User user = new User(
					resultSet.getString("no"),
					resultSet.getString("no"),
					teacher
			);
			UserService.getInstance().add(connection,user);
		}catch (SQLException e){
			e.printStackTrace();
			try {
				//�ع���ǰ���������Ĳ���
				if(connection !=null){
					connection.rollback();
				}
			}
			catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try {
				//�ָ��Զ��ύ
				if (connection!=null){
					connection.setAutoCommit(true);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			//�ر�
			JdbcHelper.close(pstmt,connection);
		}
		return affectedRowNum>0;
	}
	public boolean delete(Integer id) throws SQLException {
		//������Ӷ���
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Boolean affected = null;
		try {
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("SELECT * FROM user where teacher_id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				UserDao.getInstance().delete(resultSet.getInt("id"));
			}
			//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM teacher WHERE ID =?");
			//ΪԤ�������������ֵ
			pstmt.setInt(1, id);
			//ִ��Ԥ��������executeUpdate()��������ȡ���Ӽ�¼������
			int affectedRowNum = pstmt.executeUpdate();
			connection.commit();
			return affectedRowNum > 0;
		}catch (SQLException e){
			if(connection != null){
				connection.rollback();
			}
		}finally {
			if(connection != null){
				connection.setAutoCommit(true);
			}
			JdbcHelper.close(preparedStatement,connection);
		}
		return affected;
	}
	public static void main(String[] args) throws SQLException {
		//���school�����Ա��departmenttoadd��school�����Ը�ֵ
        ProfTitle profTitle = ProfTitleService.getInstance().find(2);
		Degree degree = DegreeService.getInstance().find(4);
		//����departmentToAdd����
		Department department = DepartmentService.getInstance().find(3);
		//����Dao����
		TeacherDao teacherDao = new TeacherDao();
		Teacher teacher = teacherDao.find(8);
        System.out.println(teacher);
		teacher.setName("��ͬ");
		teacherDao.update(teacher);
		//ִ��Dao����ķ���
		Teacher teacher1 = teacherDao.find(8);
		//��ӡ��Ӻ󷵻صĶ���
		System.out.println(teacher1);
		System.out.println("�޸�Teacher����ɹ���");
	}
}
