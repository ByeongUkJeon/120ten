package controllers;

import java.io.IOException;
import java.util.List;

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
import models.vaildators.UserValidate;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class SignupServlet
 */
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginedUser")!= null) {
            response.sendRedirect("index.jsp"); 
            return;
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/signup.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        em.getTransaction().begin();
        

        String account = request.getParameter("account");
 
        
        User checkUser = null;
        try {
            checkUser = em.createNamedQuery("accountCheck", User.class)
                    .setParameter("account", account)
                    .getSingleResult();
            if (checkUser != null) {
                return;
            }
        } catch (NoResultException ex) {
            User u = new User(); 
            u.setAccount(account);
            
            String nickname = request.getParameter("nickname");
            u.setUsername(nickname);

            String password = request.getParameter("password");
            String hashedPassword = EncryptUtil.sha256(password);
            u.setPassword(hashedPassword);
            String confirmPassword = EncryptUtil.sha256(request.getParameter("confirmpassword"));
            
            List<String> errors = UserValidate.validate(u, confirmPassword);

            if(errors.size() > 0) {
                em.close();

                request.setAttribute("user", u);
                request.setAttribute("errors", errors);
                for (String error : errors) {
                    System.out.println(error);
                }
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/signup.jsp");
                rd.forward(request, response);
            } else {
                em.persist(u);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "登録が完了しました。");
                em.close();

                response.sendRedirect("index");
            }
        }

        

    }

}
