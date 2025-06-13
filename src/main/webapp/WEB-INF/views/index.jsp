<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>120点</title>
  <link rel="stylesheet" href="css/style.css">
  <style>
  .gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
  margin-top: 20px;
}

.photo-card {
  width: 180px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  overflow: hidden;
  text-align: center;
}

.photo-card img {
  width: 100%;
  height: auto;
  display: block;
  border-bottom: 1px solid #ddd;
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
<div class="main">
  <div class="container">
        <h3>あなたの写真は何点？<br>今すぐ採点を！</h3>
        <button type="submit" onClick="location.href='upload'">写真アップロード</button>
  </div>
</div>
  <h2 style="text-align:center;">📷今日の写真</h2>
<div class="gallery">
  <c:forEach var="photo" items="${photos}">
    <div style=" cursor: pointer;" onclick="location.href='photo?id=${photo.id}'" class="photo-card">
      <img src="photos/${photo.imagePath}" alt="alt" />
      <div class="caption">🎨${photo.score}点</div>
    </div>
    </c:forEach>
  <c:if test="${empty photos }">
    <h2>登録されている写真がありません!</h2>
  </c:if>
</div>


  <%@ include file="/layout/footer.jsp" %>
</body>
</html>
