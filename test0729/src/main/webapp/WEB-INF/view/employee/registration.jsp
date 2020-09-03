<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<title>직원 등록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<form:form modelAttribute="employee" action="registration.test" method="post">
<table>
<tr><td colspan="2">회원가입
<spring:hasBindErrors name="employee">
      <c:forEach items="${errors.globalErrors }" var="error">
        <br><spring:message code="${error.code }"/>
      </c:forEach>
  </spring:hasBindErrors>
</td></tr>
<tr><th>사번</th><td><form:input path="number"/><form:errors path="number" /></td></tr>
<tr><th>이름</th><td><form:input path="name"/><form:errors path="name" /></td></tr>
<tr><th>집주소1</th><td><form:input path="addr1"/><form:errors path="addr1" /></td></tr>
<tr><th>집주소2</th><td><form:input path="addr2"/><form:errors path="addr2" /></td></tr>
<tr><th>우편번호</th><td><form:input path="zipCode"/><form:errors path="zipCode" /></td></tr>
<tr><th>태어난 연도</th><td><form:input path="birtyYear"/><form:errors path="birtyYear" /></td></tr>
<tr><th>팀 번호</th><td><form:input path="teamId"/><form:errors path="teamId" /></td></tr>
<tr><th>입사일</th><td><form:input path="joinedDate"/><form:errors path="joinedDate" /></td></tr>
<tr><td colspan="2"><input type="submit" value="등록" /></td></tr>
</table>
</form:form>
</body>
</html>