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

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    //POST,http://49.235.26.77:8080/teacher.ctl,������ʦ
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String teacher_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪTeacher����
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //teacherToAdd.setId(4+(int)(Math.random()*100));
        System.out.println(teacherToAdd);
        JSONObject message = new JSONObject();
        try {
            TeacherService.getInstance().add(teacherToAdd);
            //message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        
    }
    //DELETE,http://49.235.26.77:8080/teacher.ctl,ɾ����ʦ
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //�����ݿ����ɾ����Ӧ��ѧԺ
        JSONObject message = new JSONObject();
        try {
            boolean deleted = TeacherService.getInstance().delete(id);
            if (deleted) {
                message.put("message", "delete successfully");
            }else {
                message.put("message", "there is no record of it");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //��Ӧ
        
    }
    //PUT,http://49.235.26.77:8080/teacher.ctl,�޸���ʦ
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪTeacher����
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�Teacher����
        try {
            TeacherService.getInstance().update(teacherToAdd);
            //����������Ϣ
            //message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //��Ӧmessage��ǰ��
        
    }
    //GET,http://49.235.26.77:8080/teacher.ctl,������ʦ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        //���id = null, ��ʾ��Ӧ����ѧλ���󣬷�����Ӧidָ����ѧλ����
        if(id_str == null){
            try {
                responseTeachers(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseTeacher(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //��Ӧһ��ѧλ����
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "get successfully");
        //��Ӧ
        response.getWriter().println(teacher_json);
    }
    //��Ӧ����ѧλ����
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "�б�ɹ�");
        //��Ӧ
        response.getWriter().println(teachers_json);
        
    }
}