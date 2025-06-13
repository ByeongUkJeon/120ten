<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="header">
  <nav>
    <c:choose>
      <c:when test="${sessionScope.loginedUser != null}">
         <c:out value="${sessionScope.loginedUser.username}" />
         &nbsp;さん、ようこそ&nbsp;&nbsp;
         <a href="${pageContext.request.contextPath}/logout" />ログアウト</a>
      </c:when>
        <c:otherwise>
            <a href="login">Login</a>
        </c:otherwise>  
    </c:choose>
  </nav>
  <h1><a href="index">120点!</a></h1>
    <a href="list?page=1">写真目録</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="ranking?view=day">ランキング</a> 
</div>
<hr>