<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/item/edit.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 수정</title>
</head>
<body>
<!-- enctype="multipart/form-data" 이면, 메서드 방식은 POST 
	modelAttribute="item" : 해당하는 id의 item 객체들만 들어옴
-->
<form:form modelAttribute="item" action="update.shop" enctype="multipart/form-data">
	<form:hidden path="id" />
	<form:hidden path="pictureUrl" />
	<h2>상품 정보 수정</h2>
	<table border="1" style="border-collapse: collapse;">
		<tr><td>상품명</td><td><form:input path="name" maxlength="20"/></td><td><font color="red"><form:errors path="name" /></font></td></tr>
		<tr><td>상품 가격</td><td><form:input path="price" maxlength="20"/></td><td><font color="red"><form:errors path="price" /></font></td></tr>
		<tr><td>상품 이미지</td><td><input type="file" name="picture" /></td><td>${item.pictureUrl}</td></tr>
		<tr><td>상품 설명</td><td><form:textarea path="description" cols="20" rows="5" /></td><td><font color="red"><form:errors path="description" /></font></td></tr>
		<tr><td colspan="3"><input type="submit" value="수정 등록">&nbsp;<input type="button" value="목록" onclick="location.href='list.shop'"></td></tr>
	</table>
</form:form>
</body>
</html>