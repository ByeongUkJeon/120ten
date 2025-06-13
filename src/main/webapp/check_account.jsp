<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*, utils.DBUtil, models.User" %>
<%@ page import="jakarta.persistence.EntityManager" %>
<%@ page import="jakarta.persistence.EntityManagerFactory" %>
<%@ page import="jakarta.persistence.Persistence" %>
<%@ page import="jakarta.persistence.NoResultException" %>

<%
    String account = request.getParameter("account");
    
    EntityManager em = DBUtil.createEntityManager();
    User u = new User();
    try {
        u = em.createNamedQuery("accountCheck", User.class)
                .setParameter("account", account)
                .getSingleResult();
    } catch (NoResultException e) {
        out.print("available");
        return;
    }
    
    if (u != null) {
        out.print("taken");
        
    }
%>
