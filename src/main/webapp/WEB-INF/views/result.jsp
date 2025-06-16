<%@ page import="com.google.cloud.vision.v1.EntityAnnotation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

<head><title>Result</title></head>
<link rel="stylesheet" href="css/style.css">
<style>

.res {
  max-width: 700px;
  margin: 40px auto;
  padding: 30px;
  background-color: #fdfdfd;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.res h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.res img {
  max-width: 100%;
  height: auto;
  border-radius: 10px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
}

.res h3 {
  font-size: 20px;
  color: #444;
  margin-top: 30px;
  margin-bottom: 10px;
  border-bottom: 2px solid #eee;
  padding-bottom: 5px;
}

.res ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.res ul li {
  background-color: #f1f3f5;
  margin: 8px 0;
  padding: 12px 16px;
  border-radius: 8px;
  color: #222;
  font-size: 16px;
  text-align: left;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}
</style>
<body>
  <%@ include file="/layout/header.jsp" %>
<div class="main">
<div class="res">


  <%-- 
  <h3>Label:</h3>
  <ul>
      <c:forEach var="label" items="${labels}">
          <li><c:out value="${label.description}"/>&nbsp;(<fmt:formatNumber value="${label.score * 100}" pattern="#0.00"/>%)</li>
      </c:forEach>
  </ul>
  --%>
  <c:choose>
  <c:when test="${empty labels}">
  
    <img src="photos/<c:out value="${filename}"/>" width="500"/>
    <h3>写真に何かしらの問題があります。</h3>
    <h2>公開できない写真です。</h2>
  </c:when>
  <c:otherwise>
    <h2>🎨 Score: <c:out value="${score}"/>点</h2>
    <img src="photos/<c:out value="${filename}"/>" width="500"/>
    <h3><c:out value="${summary}"/></h3>
    <form action="<c:url value='/save' />" method="post">
      <input type="hidden" name="filename" value="${filename}" />
      <input type="hidden" name="score" value="${score}" />
      <input type="hidden" name="summary" value="${summary}" />
      <button type="submit">写真を公開</button>
    </form>
  </c:otherwise>
  </c:choose>
</div>
</div>

  <%@ include file="/layout/footer.jsp" %>
</body>
</html>
