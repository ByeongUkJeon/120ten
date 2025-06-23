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
 * Servlet implementation class SearchServlet
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            
            String keyword = request.getParameter("keyword");
            EntityManager em = DBUtil.createEntityManager();
            int page = 1;
            
            List<Photo> photos = em.createNamedQuery("getSearchPhoto", Photo.class).setParameter("keyword", "%"+ keyword +"%").setFirstResult(12 * (page - 1))
                    .setMaxResults(12).getResultList();
            request.setAttribute("photos", photos);
            
            long photo_count = (long)em.createNamedQuery("getSearchPhotoCount", Long.class).setParameter("keyword", "%"+ keyword +"%")
                    .getSingleResult();
            request.setAttribute("photo_count", photo_count);

            boolean searched = true;
            request.setAttribute("searched",searched);
            request.setAttribute("keyword",keyword);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/list.jsp");
            rd.forward(request, response);
        }
        catch (Exception e){
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
