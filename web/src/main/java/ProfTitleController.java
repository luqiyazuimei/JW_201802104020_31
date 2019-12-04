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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    //POST,http://49.235.26.77:8080/profTitle.ctl,����ְ��
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String profTitle_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪProfTitle����
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        profTitleToAdd.setId(4+(int)(Math.random()*100));
        System.out.println(profTitleToAdd);
        JSONObject message = new JSONObject();
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
            //message.put("statusCode", "200");
            message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        
    }
    //DELETE,http://49.235.26.77:8080/profTitle.ctl,ɾ��ְ��
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //�����ݿ����ɾ����Ӧ��ѧԺ
        JSONObject message = new JSONObject();
        try {
            ProfTitleService.getInstance().delete(id);
            message.put("statusCode", "200");
            message.put("message", "delete successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        
    }
    //PUT,http://49.235.26.77:8080/profTitle.ctl,�޸�ְ��
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪProfTitle����
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�ProfTitle����
        try {
            ProfTitleService.getInstance().update(profTitleToAdd);
            //����������Ϣ
            message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("data", null);
        }
        //��Ӧmessage��ǰ��
        
    }
    //GET,http://49.235.26.77:8080/profTitle.ctl,����ְ��
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        //���id = null, ��ʾ��Ӧ����ѧλ���󣬷�����Ӧidָ����ѧλ����
        if(id_str == null){
            try {
                responseProfTitles(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseProfTitle(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //��Ӧһ��ѧλ����
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "get successfully");

        //��Ӧ
        response.getWriter().println(profTitle_json);
        
    }
    //��Ӧ����ѧλ����
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "�б�ɹ�");
        //��Ӧmessage��ǰ��
        response.getWriter().println(profTitles_json);
        
    }
}