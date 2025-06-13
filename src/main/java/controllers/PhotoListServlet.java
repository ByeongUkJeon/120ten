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

import models.Photo;
import utils.DBUtil;

/**
 * Servlet implementation class PhotoListServlet
 */
@WebServlet("/list")
public class PhotoListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            EntityManager em = DBUtil.createEntityManager();
            
            int page = 1;
            
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {}
            
            
            List<Photo> photos = em.createNamedQuery("getAllPhotos", Photo.class)
                    .setFirstResult(12 * (page - 1))
                    .setMaxResults(12)
                    .getResultList();
            
            long photo_count = (long)em.createNamedQuery("getPhotoCount", Long.class)
                    .getSingleResult();
            
            em.close();
            
            request.setAttribute("photos", photos);
            request.setAttribute("photo_count", photo_count);
            request.setAttribute("page", page);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/list.jsp");
            rd.forward(request, response);
             
        } catch (Exception e) {
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
