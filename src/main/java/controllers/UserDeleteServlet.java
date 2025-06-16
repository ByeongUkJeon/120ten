package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.persistence.EntityManager;
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
 * Servlet implementation class UserDeleteServlet
 */
@WebServlet("/deleteuser")
public class UserDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            User loginedUser = (User) session.getAttribute("loginedUser");
            EntityManager em = DBUtil.createEntityManager();
            
            em.getTransaction().begin();
            List<Comment> comments = em.createNamedQuery("getCommentsByUser", Comment.class)
                    .setParameter("id", loginedUser.getId())
                    .getResultList();
            for (Comment c : comments) {
                em.remove(c);
            }
            List<Like> likes= em.createNamedQuery("getLikesByUser", Like.class)
                    .setParameter("id", loginedUser.getId())
                    .getResultList();
            for (Like l : likes) {
                em.remove(l);
            }
            
            List<Photo> photos = em.createNamedQuery("getPhotoUser", Photo.class)
                    .setParameter("id", loginedUser.getId())
                    .getResultList();
            for (Photo p: photos) {
                comments = em.createNamedQuery("getComments", Comment.class)
                        .setParameter("photoid", p.getId())
                        .getResultList();
                for (Comment c : comments) {
                    em.remove(c);
                }
                likes= em.createNamedQuery("getLikes", Like.class)
                        .setParameter("photo_id", p.getId())
                        .getResultList();
                for (Like l : likes) {
                    em.remove(l);
                }
                em.remove(p);
            }
            User u = em.find(User.class, loginedUser.getId());
            session.invalidate();
            em.remove(u);
            em.getTransaction().commit();
            em.close();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('今までありがとうございました。'); location.href='index';</script>"); 
            writer.close();
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
