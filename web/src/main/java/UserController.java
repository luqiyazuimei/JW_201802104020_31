import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user")
public class UserController extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪTeacher����
        User userToAdd = JSON.parseObject(user_json, User.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�Teacher����
        try {
            UserService.getInstance().changePassword(userToAdd);
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //��Ӧmessage��ǰ��
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //�������ѧԺ
        try{
        Collection<User> users = UserService.getInstance().findAll();
        String users_json = JSON.toJSONString(users);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "�б�ɹ�");
        //��Ӧ
        response.getWriter().println(users_json);
        } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}

