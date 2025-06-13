<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>AI</title></head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/style.css">
<body>
  <%@ include file="/layout/header.jsp" %>
<div class="main">
    <div class="container">
  <h2>Upload</h2>
  <form method="post" action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data">
    <input type="file" name="image" accept="image/*" required />
    <input type="submit" value="アップロード" />
  </form>
  </div>
</div>
  <%@ include file="/layout/footer.jsp" %>
</body>
</html>
