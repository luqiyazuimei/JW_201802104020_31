import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //����JSON����message���Ա���ǰ̨������Ϣ
        JSONObject message = new JSONObject();
        try{
            User loggedUser = UserService.getInstance().userLogin(username,password);
            if (loggedUser != null){
                message.put("message","��¼�ɹ�");
                HttpSession session = request.getSession();
                //10������û�в�������ʹsessionʧЧ
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
                response.getWriter().println(message);
                //�ض�������ҳ
                return;
            }
            else {
                message.put("message","�û������������");
            }
        } catch (SQLException e) {
            message.put("message","���ݿ�����쳣");
            e.printStackTrace();
        } catch (Exception e){
            message.put("message","�����쳣");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
