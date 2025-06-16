package controllers;

import java.io.IOException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class CheckAccountServlet
 */
@WebServlet("/checknickname")
public class CheckNicknameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckNicknameServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        
        EntityManager em = DBUtil.createEntityManager();
        User u = new User();
        try {
            u = em.createNamedQuery("nicknameCheck", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
        } catch (NoResultException e) {
            response.getWriter().print("available");
            return;
        }
        
        if (u != null) {
            response.getWriter().print("taken");
            
        }
    }


}
