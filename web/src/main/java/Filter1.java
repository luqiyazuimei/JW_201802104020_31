import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        boolean contains = path.contains("/login")|| path.contains("myapp");
        String method = request.getMethod();
        if(!path.contains("myapp")){
            if (method.equals("PUT") || method.equals("POST")){
                //������Ӧ�ַ�����ΪUTF-8
                response.setContentType("text/html;charset=UTF-8");
                //���������ַ�����ΪUTF-8
                request.setCharacterEncoding("UTF-8");
            }else {
                //���������ַ�����ΪUTF-8
                response.setContentType("text/html;charset=UTF-8");
            }
        }
        chain.doFilter(req, resp);
        System.out.println("Filter1 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException {
    }
}
