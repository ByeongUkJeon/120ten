<%@ page import="com.google.cloud.vision.v1.EntityAnnotation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Mypage</title>
 <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <%@ include file="/layout/header.jsp" %>


<div class="main">
  <div class="container">
    <h2><c:out value="${user.username}"/>さんのマイページ</h2>  
  </div>
</div>
</form>
  <%@ include file="/layout/footer.jsp" %>
</body>


</html>
