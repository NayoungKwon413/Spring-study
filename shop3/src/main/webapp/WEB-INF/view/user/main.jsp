<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/user/main.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보</title>
</head>
<body>
<h2>환영합니다. ${sessionScope.loginUser.username}님</h2>
<a href="mypage.shop?id=${loginUser.userid}">my page</a><br>
<a href="logout.shop">logout</a>
</body>
</html>