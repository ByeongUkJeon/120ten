package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Photo;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class MypageServlet
 */
@WebServlet("/mypage")
public class MypageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MypageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            
            User u = (User) session.getAttribute("loginedUser");
            
            request.setAttribute("user", u);
            EntityManager em = DBUtil.createEntityManager();
            
            List<Photo> photos = em.createNamedQuery("getPhotoUser", Photo.class).setParameter("id", u.getId()).getResultList();
            request.setAttribute("photos", photos);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/mypage.jsp");
            rd.forward(request, response);
           
        } catch (Exception e){
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>");
            writer.println("alert('エラーが発生しました。');");
            writer.println("window.location.href = 'index';");
            writer.println("</script>");
            writer.close();    
        }
    }

}
