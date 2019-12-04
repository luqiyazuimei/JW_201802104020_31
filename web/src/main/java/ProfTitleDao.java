import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();

	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	public Collection<ProfTitle> findAll() throws SQLException {
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		//������Ӷ���
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
		ResultSet resultSet = statement.executeQuery("SELECT * FROM PROFTITLE");
		//���������Ȼ����һ����¼����ִ��ѭ����
		while (resultSet.next()){
			//����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"));
			//��degrees���������Degree����
			profTitles.add(profTitle);
		}
		//�ر���Դ
		JdbcHelper.close(resultSet,statement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException{
	    ProfTitle profTitle = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM PROFTITLE WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return profTitle;
    }

	public boolean add (ProfTitle profTitle) throws SQLException{
		//������������
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO PROFTITLE (ID,NO,DESCRIPTION,REMARKS) VALUES" +
						"(?,?,?,?)");
		preparedStatement.setInt(1,profTitle.getId());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getDescription());
		preparedStatement.setString(4,profTitle.getRemarks());
		int affectRowNum = preparedStatement.executeUpdate();
		System.out.println("������ " + affectRowNum + " �С�");
		JdbcHelper.close(preparedStatement,connection);
		return affectRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException{
	Connection connection = JdbcHelper.getConn();
	//����sql��䣬��������Ϊռλ��
	String deleteProfTitle_sql = "DELETE FROM PROFTITLE WHERE ID = ?";
	//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
	PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
	//ΪԤ�������������ֵ
	pstmt.setInt(1,id);
	//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
	int affectedRowNum = pstmt.executeUpdate();
	System.out.println("ɾ���� "+affectedRowNum+" ��");
	JdbcHelper.close(pstmt,connection);
	return affectedRowNum > 0;
}

	public boolean update(ProfTitle profTitle) throws SQLException {
	Connection connection = JdbcHelper.getConn();
	//����sql��䣬��������Ϊռλ��
	String updateDepartment_sql = "UPDATE PROFTITLE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
	//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
	PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
	//ΪԤ�������������ֵ
    pstmt.setString(1,profTitle.getDescription());
    pstmt.setString(2,profTitle.getNo());
    pstmt.setString(3,profTitle.getRemarks());
    pstmt.setInt(4,profTitle.getId());
	//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
	int affectedRowNum = pstmt.executeUpdate();
	System.out.println("�޸��� "+affectedRowNum+" ��");
	JdbcHelper.close(pstmt,connection);
	return affectedRowNum > 0;
}

	public static void main(String[] args) throws SQLException {
		ProfTitleDao profTitleDao = new ProfTitleDao();
		//ִ��Dao����ķ���
		ProfTitle profTitle = profTitleDao.find(3);
		System.out.println(profTitle);
		profTitle.setDescription("˶ʿ");
		profTitleDao.update(profTitle);
		ProfTitle profTitle1 = profTitleDao.find(3);
//		//��ӡ��Ӻ󷵻صĶ���
		System.out.println(profTitle1);
		System.out.println("�޸�ProfTitle����ɹ���");
	}
}

