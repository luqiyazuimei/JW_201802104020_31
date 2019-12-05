import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter1",
    urlPatterns = {"/*"})
public class Filter1 implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getRequestURI();
                request.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
        if (!path.contains("/login") && !path.contains("/logout")){
            JSONObject message = new JSONObject();
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null){
                message.put("message","ÇëµÇÂ¼»òÖØÐÂµÇÂ¼");
                response.getWriter().println(message);
                return;
            }
        }
        chain.doFilter(req, resp);
        System.out.println("Filter1 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException {
    }
}
