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
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TEACHER");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()) {
			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
					profTitle, degree, department,resultSet.getString("no"));
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}
    public Teacher find(Integer id) throws SQLException{
	    Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM TEACHER WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
            Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),
                    profTitle,degree,department,resultSet.getString("no"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,pstmt,connection);
        return teacher;
    }
	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String updateTeacher_sql = "UPDATE TEACHER SET NO=?, NAME = ?, PROFTITLE_ID = ?, DEGREE_ID = ?, DEPARTMENT_ID = ? WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(updateTeacher_sql);
		//为预编译的语句参数赋值
        pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		pstmt.setString(5,teacher.getNo());
		pstmt.setInt(6,teacher.getId());
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 行");
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
			//添加教师对象
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
				//回滚当前连接所做的操作
				if(connection !=null){
					connection.rollback();
				}
			}
			catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection!=null){
					connection.setAutoCommit(true);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			//关闭
			JdbcHelper.close(pstmt,connection);
		}
		return affectedRowNum>0;
	}
	public boolean delete(Integer id) throws SQLException {
		//获得连接对象
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
			//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM teacher WHERE ID =?");
			//为预编译的语句参数赋值
			pstmt.setInt(1, id);
			//执行预编译对象的executeUpdate()方法，获取增加记录的行数
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
		//获得school对象，以便给departmenttoadd的school的属性赋值
        ProfTitle profTitle = ProfTitleService.getInstance().find(2);
		Degree degree = DegreeService.getInstance().find(4);
		//创建departmentToAdd对象
		Department department = DepartmentService.getInstance().find(3);
		//创建Dao对象
		TeacherDao teacherDao = new TeacherDao();
		Teacher teacher = teacherDao.find(8);
        System.out.println(teacher);
		teacher.setName("苏同");
		teacherDao.update(teacher);
		//执行Dao对象的方法
		Teacher teacher1 = teacherDao.find(8);
		//打印添加后返回的对象
		System.out.println(teacher1);
		System.out.println("修改Teacher对象成功！");
	}
}
