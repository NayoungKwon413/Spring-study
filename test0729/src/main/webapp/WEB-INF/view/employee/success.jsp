<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>직원 등록 완료</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<table>
<tr><td colspan="2">${employee.name } 님의 직원 등록이 완료 되었습니다.</td></tr>
<tr><th>사번</th><td>${employee.number }</td></tr>
<tr><th>이름</th><td>${employee.name }</td></tr>
<tr><th>집주소1</th><td>${employee.addr1 }</td></tr>
<tr><th>집주소2</th><td>${employee.addr2 }</td></tr>
<tr><th>우편번호</th><td>${employee.zipCode }</td></tr>
<tr><th>태어난 연도</th><td>${employee.birtyYear }</td></tr>
<tr><th>팀 번호</th><td>${employee.teamId }</td></tr>
<tr><th>입사일</th>
<td><fmt:formatDate value="${employee.joinedDate}" pattern="yyyy-MM-dd"/> </td></tr>
</table>
</body>
</html>