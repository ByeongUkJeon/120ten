package controllers;

import java.io.IOException;
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
 * Servlet implementation class RankingServlet
 */
@WebServlet("/ranking")
public class RankingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RankingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String view = request.getParameter("view");
        EntityManager em = DBUtil.createEntityManager();
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();            
        LocalDateTime end = today.plusDays(1).atStartOfDay();  


        if(view.equals("weekly")) {
            start = today.minusDays(today.getDayOfWeek().getValue()).atStartOfDay();      
            System.out.println(start);
            end = today.plusDays(7 - today.getDayOfWeek().getValue()).atStartOfDay();      
            System.out.println(end);

        }
        
        else if(view.equals("monthly")) {
            start = today.minusDays(today.getDayOfMonth() - 1).atStartOfDay();      
            System.out.println(start);
            end = today.plusDays(today.lengthOfMonth() - today.getDayOfMonth() + 1).atStartOfDay();
            System.out.println(end);
       
        }

        List<Photo> photos = em.createNamedQuery("getRankingPhotos", Photo.class).setParameter("start", start).setParameter("end", end).setMaxResults(20)
                .getResultList();
        
        em.close();
        request.setAttribute("view", view);
        request.setAttribute("photos", photos);
        
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/ranking.jsp");
        rd.forward(request, response);   
    }


}
