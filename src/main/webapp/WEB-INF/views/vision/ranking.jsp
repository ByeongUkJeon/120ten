<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>120点</title>
  <link rel="stylesheet" href="css/style.css">
  <style>
.ranking-wrapper {
  display: grid;
  grid-template-columns: 1fr; /* 무조건 한 줄에 하나 */
  gap: 20px;
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}


.top3-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  justify-items: center;
}

.others-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 20px;
}

.ranking-card {
  width: 100%;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  overflow: hidden;
  text-align: center;
  display: flex;
  flex-direction: column;
  margin-bottom:10px;
}
.ranking-card img {
  width: 100%;
  height: 300px;
  object-fit: cover; /* 이미지가 작아도 채워줌 */
}

.rank-badge {
  font-weight: bold;
  padding: 6px 12px;
  border-radius: 20px;
  display: inline-block;
  color: white;
  font-size: 14px;
  margin-top: 10px;
  margin-bottom: 10px;
}

.rank-badge.gold { background: linear-gradient(to right, #FFD700, #FFC700); }
.rank-badge.silver { background: linear-gradient(to right, #C0C0C0, #B0B0B0); }
.rank-badge.bronze { background: linear-gradient(to right, #CD7F32, #C07020); }
.score {
  margin-top: 10px;
  font-size: 14px;
  color: #333;
}

.ranking-card.large {
  transform: scale(1.05);
  border: 2px solid #FFD700;
}

.ranking-card.large .rank-badge {
  font-size: 20px;
}

.ranking-card.large .score {
  font-size: 18px;
  font-weight: bold;
}
.ranking-tabs {
  text-align: center;
  margin-top: 20px;
}

.tab-button {
  display: inline-block;
  margin: 0 10px;
  padding: 10px 20px;
  border: 2px solid #ccc;
  border-radius: 25px;
  text-decoration: none;
  color: #333;
  background-color: #f9f9f9;
  transition: 0.3s;
}
.active-tab {
  background-color: #4CAF50;
  color: white;
  border-color: #4CAF50;
}
.score {
  margin: 8px 0;
  font-weight: bold;
  color: #333;
}

.meta {
  font-size: 13px;
  color: #666;
  margin-top: 6px;
}

.meta span {
  display: block;
  margin: 2px 0;
}
  </style>
</head>
<body>
  <%@ include file="/layout/header.jsp" %>
<div class="ranking-tabs">
  <a href="ranking?view=daily" class="tab-button <c:if test="${view eq 'daily'}">active-tab</c:if>">日間</a>
  <a href="ranking?view=weekly" class="tab-button <c:if test="${view eq 'weekly'}">active-tab</c:if>">週間</a>
  <a href="ranking?view=monthly" class="tab-button <c:if test="${view eq 'monthly'}">active-tab</c:if>">月間</a>
</div>
  <h2 style="text-align:center;">🏆 
  <c:if test="${view eq 'daily'}">今日</c:if>
  <c:if test="${view eq 'weekly'}">今週</c:if>
  <c:if test="${view eq 'monthly'}">今月</c:if>
  のランキング</h2>
<div class="gallery">
  <c:choose>
     <c:when test="${empty photos }">
        <h2 style="text-align: center;">登録されている写真がありません!</h2>
     </c:when>
     <c:otherwise>
        <div class="ranking-wrapper">
        
          <div class="top3-grid">
            <c:forEach var="photo" items="${photos}" varStatus="status">
              <c:if test="${status.index lt 3}">
                <div onclick="location.href='photo?id=${photo.id}'" class="ranking-card large">
                  <div class="rank-badge   <c:choose>
    <c:when test="${status.index == 0}"> gold</c:when>
    <c:when test="${status.index == 1}"> silver</c:when>
    <c:when test="${status.index == 2}"> bronze</c:when>
  </c:choose>">#${status.index + 1}</div>
                  <img src="photos/${photo.imagePath}" />
                  <div class="score">スコア: ${photo.score} 点</div>
                  <div class="meta">
                    <span class="author">👤 ${photo.user.username}</span>
                    <span class="date">📅 ${photo.createdAt}</span>
                  </div>
                </div>
              </c:if>
            </c:forEach>
          </div>
        
          <div class="others-grid">
            <c:forEach var="photo" items="${photos}" varStatus="status">
              <c:if test="${status.index ge 3}">
                <div class="ranking-card" onclick="location.href='photo?id=${photo.id}'">
                  <div class="rank-badge">#${status.index + 1}</div>
                  <img src="photos/${photo.imagePath}" />
                  <div class="score">スコア: ${photo.score} 点</div>
                  <div class="meta">
                    <span class="author">👤 ${photo.user.username}</span>
                    <span class="date">📅 ${photo.createdAt}</span>
                  </div>
                </div>
              </c:if>
            </c:forEach>
          </div>
        
        </div>
    
    <div id="pagination">
        <c:forEach var="i" begin="1" end="${((photo_count - 1) / 12) + 1}" step="1">
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
