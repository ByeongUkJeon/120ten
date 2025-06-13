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

import models.Comment;
import models.Like;
import models.Photo;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class PhotoReadServlet
 */
@WebServlet("/photo")
public class PhotoReadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoReadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            
            EntityManager em = DBUtil.createEntityManager();
            
            Photo photo = em.find(Photo.class, Integer.parseInt(request.getParameter("id")));
            User user = photo.getUser();
            List<Comment> comments = em.createNamedQuery("getComments", Comment.class)
                    .setParameter("photoid", request.getParameter("id")).getResultList();
            boolean isWriter = false;
            long likeCount = (long) em.createNamedQuery("getLikeCounts")
                    .setParameter("photo_id", request.getParameter("id"))
                    .getSingleResult();
            


            boolean liked = true;
            
            if (session != null && session.getAttribute("loginedUser") != null) {
                User loginedUser = (User) session.getAttribute("loginedUser");
                if (loginedUser.getAccount().equals(user.getAccount())) {
                    isWriter = true;
                }
                List<Like> like = em.createNamedQuery("getLikeCheck", Like.class)
                        .setParameter("photo_id", request.getParameter("id"))
                        .setParameter("user_id", loginedUser.getId())
                            .getResultList();
                liked = like.isEmpty();
                request.setAttribute("loginedUser", session.getAttribute("loginedUser"));
            }
            else {
                liked = true;
            }
            em.close();
            
            request.setAttribute("photo", photo);
            request.setAttribute("writer", user);
            request.setAttribute("comments", comments);
            request.setAttribute("likecount", likeCount);
            request.setAttribute("liked", liked);


            request.setAttribute("isWriter", isWriter);

            
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/vision/show.jsp");
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
