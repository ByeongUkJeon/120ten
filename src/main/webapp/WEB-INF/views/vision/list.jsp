<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
    width: 220px;
    height:250px;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
    background: white;
    text-align: center;
    transition: transform 0.2s ease;
}

.photo-card img {
    width: 100%;
    height: 60%;
    display: block;
    border-bottom: 1px solid #eee;
}

.photo-score {
    font-size: 16px;
    font-weight: bold;
    color: #2c3e50;
    padding: 8px 0;
}

.photo-meta {
    font-size: 13px;
    color: #666;
    padding-bottom: 10px;
}

.photo-meta span {
    display: block;
    margin: 2px 0;
}

.caption {
    padding: 8px;
    font-size: 14px;
    color: #333;
    background-color: #f9f9f9;
}

#pagination {
    text-align: center;
    margin-top: 20px;
}
</style>
</head>
<body>
    <%@ include file="/layout/header.jsp"%>

    <h2 style="text-align: center;">
    <c:choose>
    <c:when test="${searched}">📷検索結果 - <c:out value="${keyword }"/></c:when>
    <c:otherwise>📷写真目録</c:otherwise>
    </c:choose>
    </h2>
    <div class="gallery">
        <c:choose>
            <c:when test="${empty photos }">
                <h2>登録されている写真がありません!</h2>
            </c:when>
            <c:otherwise>
                <c:forEach var="photo" items="${photos}">
                    <div style="cursor: pointer;"
                        onclick="location.href='photo?id=${photo.id}'" class="photo-card">
                        <img src="photos/${photo.imagePath}" alt="alt" />
                        <div class="photo-score">🎨${photo.score}点</div>
                        <div class="photo-meta">
                            <span class="author">👤 ${photo.user.username}</span> <span
                                class="date">📅 ${photo.createdAt}</span>
                        </div>
                    </div>
                </c:forEach>

       </c:otherwise>
    </c:choose>
    </div>  
    <div id="pagination">
        <c:forEach var="i" begin="1" end="${((photo_count - 1) / 12) + 1}"
            step="1">
            <c:choose>
                <c:when test="${i == page}">
                    <c:out value="${i}" />&nbsp;
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/list?page=${i}"><c:out
                            value="${i}" /></a>&nbsp;
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>



    <%@ include file="/layout/footer.jsp"%>
</body>
</html>
