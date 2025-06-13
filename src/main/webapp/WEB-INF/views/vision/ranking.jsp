<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>120点</title>
  <link rel="stylesheet" href="css/style.css">
  <style>
.ranking-container {
  max-width: 900px;
  margin: 40px auto;
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  text-align: center;
}

.ranking-list {
  list-style: none;
  padding: 0;
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  justify-content: center;
}

.ranking-item {
  width: 180px;
  background-color: #fdfdfd;
  border: 1px solid #eee;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  overflow: hidden;
  text-align: center;
  transition: transform 0.2s ease;
}

.ranking-item:hover {
  transform: translateY(-4px);
}

.ranking-item img {
  width: 100%;
  height: auto;
  display: block;
  border-bottom: 1px solid #eee;
}

.rank-badge {
  background-color: gold;
  color: #222;
  font-weight: bold;
  padding: 6px;
  font-size: 16px;
}

.score {
  padding: 10px;
  font-size: 15px;
  color: #333;
}


.caption {
  padding: 8px;
  font-size: 14px;
  color: #333;
  background-color: #f9f9f9;
}
  </style>
</head>
<body>
  <%@ include file="/layout/header.jsp" %>

  <h2 style="text-align:center;">📷写真目録</h2>
<div class="gallery">
  <c:choose>
     <c:when test="${empty photos }">
        <h2>登録されている写真がありません!</h2>
     </c:when>
     <c:otherwise>
        <div class="ranking-container">
          <h2>🏆 今日のランキング</h2>
          <ul class="ranking-list">
            <c:forEach var="photo" items="${photos}" varStatus="status">
              <li class="ranking-item">
                <div class="rank-badge">#${status.index + 1}</div>
                <img src="photos/${photo.imagePath}" alt="photo" onclick="location.href='photo?id=${photo.id}'/>
                <div class="score">スコア: ${photo.score} 点</div>
              </li>
            </c:forEach>
          </ul>
        </div>
       
    <div id="pagination">
        <c:forEach var="1" begin="1" end="${((photo_count - 1) / 12) + 1}" step="1">
             <c:choose>
                <c:when test="${i == page}">
                    <c:out value="${i}" />&nbsp;
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/list?page=${i}"><c:out value="${i}" /></a>&nbsp;
                </c:otherwise>
              </c:choose>
        </c:forEach>
    </div>
     </c:otherwise>
    </c:choose>
</div>


  <%@ include file="/layout/footer.jsp" %>
</body>
</html>
