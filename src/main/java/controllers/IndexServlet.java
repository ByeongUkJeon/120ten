package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.Photo;
import utils.DBUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            EntityManager em = DBUtil.createEntityManager();
            
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();            
            LocalDateTime end = today.plusDays(1).atStartOfDay();  

            List<Photo> photos = em.createNamedQuery("getPhotoToday", Photo.class).setParameter("start", start).setParameter("end", end)
                    .getResultList();
            
            em.close();

            request.setAttribute("photos", photos);
            
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
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
