package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Like;
import models.Photo;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class LikeServlet
 */
@WebServlet("/like")
public class LikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
 
            int photoId = Integer.parseInt(request.getParameter("photoId"));
            User user = (User) session.getAttribute("loginedUser");

            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            boolean liked = false;
            try {
                Like like = em.createNamedQuery("getLikeCheck", Like.class)
                    .setParameter("photo_id", photoId)
                    .setParameter("user_id", user.getId())
                    .getSingleResult();

                em.remove(like);
            } catch (NoResultException e) {
                Like newLike = new Like();
                newLike.setPhoto(em.find(Photo.class, photoId));
                newLike.setUser(user);
                em.persist(newLike);
                liked = true;
            }

            long likeCount = (long) em.createNamedQuery("getLikeCounts")
                .setParameter("photo_id", photoId)
                .getSingleResult();

            em.getTransaction().commit();
            em.close();

            response.setContentType("application/json");
            response.getWriter().write("{\"liked\":" + liked + ",\"likeCount\":" + likeCount + "}");
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
