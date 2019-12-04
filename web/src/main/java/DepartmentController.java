import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    private void responseDepartmentOfSchool(int id,HttpServletResponse response)
            throws ServletException,IOException,SQLException{
        JSONObject message = new JSONObject();
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(id);
        String department_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        message.put("statusCode","200");
        message.put("message","���ҳɹ�");
        message.put("data",department_json);
        response.getWriter().println(department_json);
    }
    //POST,http://49.235.26.77:8080/department.ctl,����ϵ
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String department_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪDepartment����
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        departmentToAdd.setId(4+(int)(Math.random()*100));
        System.out.println(departmentToAdd);
        JSONObject message = new JSONObject();
        try {
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("statusCode", "200");
            //message.put("message", "add successfully");
        } catch (SQLException e) {
            e.printStackTrace();
           // message.put("data", null);
        }
        
    }
    //DELETE,http://49.235.26.77:8080/department.ctl,ɾ��ϵ
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //�����ݿ����ɾ����Ӧ��ѧԺ
        JSONObject message = new JSONObject();
        try {
            boolean deleted = DepartmentService.getInstance().delete(id);
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
        response.setContentType("html/text;charset=UTF8");
        
    }
    //PUT,http://49.235.26.77:8080/department.ctl,�޸�ϵ
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String department_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪDepartment����
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //���Ӽ�Department����
        try {
            DepartmentService.getInstance().update(departmentToAdd);
            //����������Ϣ
            //message.put("statusCode", "200");
            message.put("message", "update successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            //message.put("data", null);
        }
        response.getWriter().println(message);
    }
    //GET,http://49.235.26.77:8080/department.ctl,����ϵ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school = request.getParameter("paraType");
        //��ȡ����id
        String id_str = request.getParameter("id");
        JSONObject message = new JSONObject();
        //���id = null, ��ʾ��Ӧ����ѧλ���󣬷�����Ӧidָ����ѧλ����
        try{
            if (id_str == null) responseDepartments(response);
            else if ("school".equals(school))
                responseDepartmentOfSchool(Integer.parseInt(id_str), response);
            else
                responseDepartment(Integer.parseInt(id_str), response);
            message.put("message","���ҳɹ�");
        } catch (SQLException e) {
            message.put("message","���ݿ�����쳣");
            
        }catch (Exception e){
            message.put("message","�����쳣");
            
        }
    }
    //��Ӧһ��ѧλ����
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "get successfully");
        //��Ӧ
        response.getWriter().println(department_json);
        
    }
    //��Ӧ����ѧλ����
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //����������Ϣ
        message.put("statusCode", "200");
        message.put("message", "�б�ɹ�");
        //��Ӧ
        response.getWriter().println(departments_json);
        
    }
}
