package controllers;

import java.io.IOException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.User;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginedUser")!= null) {
            response.sendRedirect("index"); 
            return;
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
        rd.forward(request, response); 

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        
        User u = null;
        try {
            String hashedPassword = EncryptUtil.sha256(password);
            u = em.createNamedQuery("userLogin", User.class)
                    .setParameter("account", account)
                    .setParameter("password", hashedPassword)
                    .getSingleResult();
            
            em.close();

            if(u != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loginedUser", u);

                response.sendRedirect("index"); 
            }
        } catch (NoResultException ex) {

            String error = "該当する情報があれません。";
            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
            rd.forward(request, response); 
        }

        




    }

}
