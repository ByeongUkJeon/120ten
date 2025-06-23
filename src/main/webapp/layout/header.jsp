<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="header">
  <nav>
    <c:choose>
      <c:when test="${sessionScope.loginedUser != null}">
         <c:out value="${sessionScope.loginedUser.username}" />
         &nbsp;さん、ようこそ&nbsp;&nbsp;
         <a href="${pageContext.request.contextPath}/mypage" />マイページへ</a>
      </c:when>
        <c:otherwise>
            <a href="login">Login</a>
        </c:otherwise>  
    </c:choose>
  </nav>
  <h1><a href="index">120点!</a></h1>
    <a href="list?page=1">写真目録</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="ranking?view=daily">ランキング</a> <br>
<div class="search-box">
  <form action="search" method="get" class="search-form">
    <input type="text" name="keyword" placeholder="検索ワード" class="search-input" required>  
    <input type="hidden" name="page" value="1" />  
    <button type="submit" class="search-button" title="検索">
      🔍
    </button>
  </form>
</div>
</div>
<hr>