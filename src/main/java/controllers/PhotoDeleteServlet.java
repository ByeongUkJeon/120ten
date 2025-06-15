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
 * Servlet implementation class PhotoDeleteServlet
 */
@WebServlet("/deletephoto")
public class PhotoDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoDeleteServlet() {
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
            EntityManager em = DBUtil.createEntityManager();
            
            em.getTransaction().begin();
            List<Comment> comments = em.createNamedQuery("getComments", Comment.class)
                    .setParameter("photoid", request.getParameter("photoid"))
                    .getResultList();
            for (Comment c : comments) {
                em.remove(c);
            }
            List<Like> likes= em.createNamedQuery("getLikes", Like.class)
                    .setParameter("photo_id", request.getParameter("photoid"))
                    .getResultList();
            for (Like l : likes) {
                em.remove(l);
            }
            
            Photo p = em.find(Photo.class, Integer.parseInt(request.getParameter("photoid")));
            em.remove(p);
            em.getTransaction().commit();
            em.close();
            
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('削除が完了しました。'); location.href='index';</script>"); 
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
