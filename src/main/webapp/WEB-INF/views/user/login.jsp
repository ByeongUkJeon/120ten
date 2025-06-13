
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <%@ include file="/layout/header.jsp" %>
<div class="main">
  <div class="container">
    <h2>Login</h2>
      <c:if test="${error != null}">
        <div class="error-box">
            <div><c:out value="${error}" /></div>
        </div>
      </c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <input type="text" name="account" placeholder="id" required><br>
      <input type="password" name="password" placeholder="password" required><br>
      <button type="submit">ログイン</button>
    </form>
    <a href="signup">登録はこちら</a>
  </div>
</div>
  <%@ include file="/layout/footer.jsp" %>
</body>
</html>
