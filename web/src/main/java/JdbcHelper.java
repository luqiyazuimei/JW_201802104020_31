import java.sql.*;

/**
 * �ṩJDBC���Ӷ�����ͷ���Դ
 */
public final class JdbcHelper {
    //private static String url =
    // "jdbc:sqlserver://localhost:1433;databaseName=bysjs;SelectMethod=Cursor;";
    private static String  url = "jdbc:mysql://localhost:3306/bysj" +
            "?useUnicode=true&characterEncoding=utf8" + //?������ָ������utf8
            "&serverTimezone=Asia/Shanghai";
    private static String user = "root";
    private static String password = "201802104020";

    // ���������������
    private JdbcHelper() {}

    //ע������
    static {
        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //ע����������
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("δ�ҵ�����������");
        }
    }

    /**
     * @return ���Ӷ���
     * @throws SQLException
     */
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    //�رա��ͷ���Դ
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {	rs.close();	}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null){	stmt.close();}
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null){	conn.close();}
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //�رա��ͷ���Դ
    public static void close(Statement stmt, Connection conn) {
        JdbcHelper.close(null,stmt,conn);
    }
}