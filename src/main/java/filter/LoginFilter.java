package filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(urlPatterns = {"/upload", "/save", "/deletephoto", "/addcomment", "/like"})
public class LoginFilter extends HttpFilter implements Filter {

    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);
        String uri = httpReq.getRequestURI();

        if (uri.contains("/login") || uri.contains("/css") || uri.contains("/signup")) {
            chain.doFilter(request, response);
            return;
        }
        
        if (session != null && session.getAttribute("loginedUser") != null) {
            chain.doFilter(request, response);
            } else {
                if (uri.contains("/like")) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.println("{\"error\": \"unauthorized\"}"); 
                    writer.close();
                } else {
                    response.setContentType("text/html; charset=UTF-8");   
                    PrintWriter writer = response.getWriter();
                    writer.println("<script>alert('ログインをしてください。'); location.href='login';</script>"); 
                    writer.close();
                }
                

        }
       

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
