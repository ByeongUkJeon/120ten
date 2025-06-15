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
@WebServlet("/checkaccount")
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
        String account = request.getParameter("account");
        
        EntityManager em = DBUtil.createEntityManager();
        User u = new User();
        try {
            u = em.createNamedQuery("accountCheck", User.class)
                    .setParameter("account", account)
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
