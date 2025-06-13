<%@ page import="com.google.cloud.vision.v1.EntityAnnotation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Signup</title>
 <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <%@ include file="/layout/header.jsp" %>


<div class="main">
  <div class="container">
      <c:if test="${not empty errors}">
        <div class="error-box">
          <c:forEach var="error" items="${errors}">
            <div><c:out value="${error}" /></div>
          </c:forEach>
        </div>
      </c:if>
    <h2>会員登録</h2>
<form action="${pageContext.request.contextPath}/signup" method="post">
  <input type="text" id ="account" name="account" placeholder="ID" value="${user.account}" onkeyup="checkUsername()" required /><br/>
  <span id="account-status"></span><br/>
  <input type="text" name="nickname" placeholder="ニックネーム" value="${user.username}" required /><br/>
  <input type="password" name="password" placeholder="暗証番号" required /><br/>
  <input type="password" name="confirmpassword" placeholder="暗証番号(確認用)" required /><br/> 
  <button id="btn" name="btn" type="submit">会員登録</button>
    </div>
</div>
</form>
  <%@ include file="/layout/footer.jsp" %>
</body>

<script>
function checkUsername() {
  const account = document.getElementById("account").value;
  const status = document.getElementById("account-status");

  fetch("checkaccount?account=" + encodeURIComponent(account))
    .then(res => res.text())
    .then(data => {
       const btn = document.getElementById("btn");
        console.log(data);
      if (data.includes("taken")) {
        status.innerHTML = "このアカウントはご利用できません。";
        status.style.color = "red";
        btn.style.backgroundColor = "#A4AAA7";
        
        btn.disabled = true;
      } else if (data.includes("available")) {
        status.innerHTML = "ご利用可能なアカウントです。";
        status.style.color = "green";
        btn.disabled = false;
        btn.style.backgroundColor = "#4CAF50";
      } else {
        status.innerHTML = "エラー発生.";
        status.style.color = "gray";
        btn.disabled = true;
      }
    });
}
</script>

</html>
