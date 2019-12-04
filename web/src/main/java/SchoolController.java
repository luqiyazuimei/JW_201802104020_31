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

@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
    //POST,http://49.235.26.77:8080/school.ctl,����ѧԺ
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String school_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪSchool����
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        schoolToAdd.setId(4+(int)(Math.random()*100));
        System.out.println(schoolToAdd);
        JSONObject message = new JSONObject();
        try {
            SchoolService.getInstance().add(schoolToAdd);
            //message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/school.ctl,ɾ��ѧԺ
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //�����ݿ����ɾ����Ӧ��ѧԺ
        JSONObject message = new JSONObject();
        try {
            SchoolService.getInstance().delete(id);
            //message.put("statusCode", "200");
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/school.ctl,�޸�ѧԺ
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪSchool����
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�School����
        try {
            SchoolService.getInstance().update(schoolToAdd);
            //����������Ϣ
            //message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }
    //GET,http://49.235.26.77:8080/school.ctl,����ѧԺ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        //���id = null, ��ʾ��Ӧ����ѧλ���󣬷�����Ӧidָ����ѧλ����
        if(id_str == null){
            try {
                responseSchools(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseSchool(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //��Ӧһ��ѧλ����
    private void responseSchool(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        //JSONObject message = new JSONObject();
        //����������Ϣ
        //message.put("statusCode", "200");
        //message.put("message", "get successfully");

        //��Ӧ
        response.getWriter().println(school_json);

    }
    //��Ӧ����ѧλ����
    private void responseSchools(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        //JSONObject message = new JSONObject();
        //����������Ϣ
        //message.put("statusCode", "200");
        //message.put("message", "�б�ɹ�");
        //��Ӧ
        response.getWriter().println(schools_json);
        //response.getWriter().println(message);
    }
}