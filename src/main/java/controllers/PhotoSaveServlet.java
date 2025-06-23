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

import models.Photo;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class PhotoSaveServlet
 */
@WebServlet("/save")
public class PhotoSaveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoSaveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fileName = request.getParameter("filename");
            int score = Integer.parseInt(request.getParameter("score"));
            String summary = request.getParameter("summary");
            EntityManager em = DBUtil.createEntityManager();
            
            em.getTransaction().begin();
            
            Photo photo = new Photo();
            
            photo.setImagePath(fileName);
            photo.setScore(score);
            
            User uploadUser = (User) request.getSession().getAttribute("loginedUser");
            photo.setUser(uploadUser);
            
            Timestamp currentTime = new Timestamp(System.currentTimeMillis() + 32400000);
            photo.setCreatedAt(currentTime);     
            
            photo.setLabel(summary);
            
            photo.setLikeCount(0);
            em.persist(photo);
            em.getTransaction().commit();
            em.close();
            String redirectUrl = "photo?id=" + photo.getId();

            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('登録が完了されました。'); location.href='"+redirectUrl+"';</script>"); 
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            
        }

    }

}
