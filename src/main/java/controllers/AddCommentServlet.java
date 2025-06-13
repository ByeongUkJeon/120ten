package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Comment;
import models.Photo;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class CommentAddServlet
 */
@WebServlet("/addcomment")
public class AddCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("loginedUser");
            
            String comm = request.getParameter("content");
            String photo_id = request.getParameter("photo_id");
            EntityManager em = DBUtil.createEntityManager();
            Photo photo = em.find(Photo.class, Integer.parseInt(photo_id));

            em.getTransaction().begin();
            
            Comment comment = new Comment();
            
            comment.setPhoto(photo);
            
            comment.setUser(user);
            
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            comment.setCreatedAt(currentTime); 
            
            comment.setComm(comm);
            
            em.persist(comment);
            em.getTransaction().commit();
            em.close();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>");
            writer.println("alert('コメントを登録しました。');");
            writer.println("window.location.href = 'photo?id=" + photo_id + "';");
            writer.println("</script>");
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
