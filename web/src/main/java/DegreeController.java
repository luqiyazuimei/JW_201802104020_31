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

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
    //POST,http://49.235.26.77:8080/degree.ctl,����ѧλ
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String degree_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪDegree����
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        JSONObject message = new JSONObject();
        degreeToAdd.setId(4+(int)(Math.random()*100));
        try {
            DegreeService.getInstance().add(degreeToAdd);
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //DELETE,http://49.235.26.77:8080/degree.ctl,ɾ��ѧλ
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //�����ݿ����ɾ����Ӧ��ѧԺ
        JSONObject message = new JSONObject();
        try {
            DegreeService.getInstance().delete(id);
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        //��Ӧ
        response.getWriter().println(message);
    }
    //PUT,http://49.235.26.77:8080/degree.ctl,�޸�ѧλ
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪDegree����
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�Degree����
        try {
            DegreeService.getInstance().update(degreeToAdd);
            //����������Ϣ
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }
    //GET,http://49.235.26.77:8080/degree.ctl,����ѧλ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        //���id = null, ��ʾ��Ӧ����ѧλ���󣬷�����Ӧidָ����ѧλ����
        if(id_str == null){
            try {
                responseDegrees(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseDegree(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //��Ӧһ��ѧλ����
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "get successfully");

        //��Ӧ
        response.getWriter().println(degree_json);

    }
    //��Ӧ����ѧλ����
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "�б�ɹ�");
        //��Ӧ
        response.getWriter().println(degrees_json);

    }
}