<%@ page import="com.google.cloud.vision.v1.EntityAnnotation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Show</title>
</head>
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
.res h5 {
    text-align:right;
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
button.delete {
  padding: 10px 20px;
  background-color: #ff4d4d;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
}

.comment-section {
  max-width: 600px;
  margin: 10px auto;
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.comment-section h3 {
  margin-bottom: 15px;
  font-size: 20px;
  color: #333;
}

.comment-form textarea {
  width: 100%;
  height: 80px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
}

.comment-form button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.comment-form button:hover {
  background-color: #45a049;
}

.comment-list {
  margin-top: 20px;
}

.comment {
  position: relative;
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.comment-author {
  font-weight: bold;
  color: #222;
  margin-bottom: 4px;
}

.comment-content {
  font-size: 14px;
  color: #555;
}
.delete-form {
  display: inline;
  margin-top: 5px;
}

.delete-btn {
  width:auto;
  background-color: #ff4d4d;
  color: white;
  border: none;
  padding: 5px 10px;
  font-size: 12px;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #e60000;
}
.like-container {
  text-align: center;
  justify-content: center;  
  margin-top: 15px;
}

.like-button {
  padding: 6px 10px;             
  font-size: 14px;               
  border: 1px solid #999;
  border-radius: 6px;
  background-color: white;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  width: auto;                
  min-width: unset;            
}

.like-button:hover {
  background-color: #f2f2f2;
}

.like-button.liked {
  border-color: #0d6efd;
  color: #0d6efd;
  background-color: #e8f0fe;
}

.like-button.disabled {
  border-color: #ccc;
  color: #aaa;
  cursor: not-allowed;
}
</style>
<body>
  <%@ include file="/layout/header.jsp" %>
<div class="main">
<div class="res">

  <h3><c:out value="${writer.username}"/>&nbsp;様の&nbsp;写真</h3>
  <h5><c:out value="${photo.createdAt }"/>にアップロード</h4>
  <h2>🎨 Score: <c:out value="${photo.score}"/>点</h2>
  <img src="photos/<c:out value="${photo.imagePath}"/>" width="500"/>
  <h3><c:out value="${photo.label}"/></h3>
  <c:if test="${isWriter}">
  <form action="<c:url value='/deletephoto' />" method="post">
    <input type="hidden" name="photoid" value="${photo.id}" />
    <input type="hidden" name="account" value="${writer.account}" />
    <button type="submit" class="delete" onClick="confirmDestroy(0);">削除</button>
</form>
  </c:if>
  <div class="like-container">
  
    <button class="like-button" onclick="toggleLike(${photo.id})" id="like-btn">
      👍 <span id="like-count">${likecount}</span>
    </button>
  </div>
<div class="comment-section">
  <h3>💬 コメント</h3>

  <form class="comment-form" method="post" action="<c:url value='/addcomment' />">
    <textarea name="content" placeholder="コメントを入力してください..." required></textarea>
    <input type="hidden" name="photo_id" value="${photo.id}" />
    <button type="submit">送信</button>
  </form>

  <div class="comment-list">
  <c:forEach var="comment" items="${comments}">
    <div class="comment">
      <div class="comment-author">${comment.user.username}</div>
      <div class="comment-content">${comment.comm }</div>
      <c:if test="${comment.user.username == loginedUser.username }">
       <form class="delete-form" method="post" action="deletecomment">
        <input type="hidden" name="commentId" value="${comment.id}" />
        <input type="hidden" name="photo_id" value="${photo.id}" />  
        <input type="hidden" name="account" value="${comment.user.account}" />
          
        <button type="submit" class="delete-btn" onclick="confirmDestroy(1);">削除</button>
      </form>
      </c:if>
    </div>
   </c:forEach>
  </div>
</div>


</div>
</div>

  <%@ include file="/layout/footer.jsp" %>
</body>
</html>

<script>

        const btn = document.getElementById("like-btn");
        <c:if test="${not liked}">
          btn.classList.add("liked");
        </c:if>
       function confirmDestroy(n) {
           if(confirm("本当に削除してよろしいですか？")) {
               document.forms[n].submit();
           }
       }
       function toggleLike(photoId) {
           fetch("like?photoId=" + photoId)
             .then(res => res.json())
             .then(data => {
               if (data.error === "unauthorized") {
                   alert("ログインをしてください。");
                   window.location.href = "login";  
                   return;
                 }
                              
               const countSpan = document.getElementById("like-count");
               const btn = document.getElementById("like-btn");

               countSpan.innerText = data.likeCount;
               if (data.liked) {
                   btn.classList.add("liked");
                 } else {
                   btn.classList.remove("liked");
                 }
             });
         }
                
 </script>
