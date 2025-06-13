package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Comment;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class DeleteCommentServlet
 */
@WebServlet("/deletecomment")
public class DeleteCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommentServlet() {
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
            if(!loginedUser.getAccount().equals(request.getParameter("account"))) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.println("<script>alert('正しくない接近です。'); location.href='index';</script>"); 
                writer.close();
                return;
            }
            String photo_id = request.getParameter("photo_id");

            EntityManager em = DBUtil.createEntityManager();
            
            em.getTransaction().begin();
            Comment comment = em.find(Comment.class, Integer.parseInt(request.getParameter("commentId")));
            em.remove(comment);
            em.getTransaction().commit();
            em.close();
            
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>");
            writer.println("alert('コメントを削除しました。');");
            writer.println("window.location.href = 'photo?id=" + photo_id + "';");
            writer.println("</script>");
            writer.close(); 
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
