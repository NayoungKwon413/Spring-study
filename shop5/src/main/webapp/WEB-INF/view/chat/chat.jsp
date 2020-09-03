<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/board/chat.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContext.request.localPort}" />
<c:set var="server" value="${pageContext.request.serverName}" />
<c:set var="path" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function(){    // 화면이 완성되면 socket 이 완성되어 server 로 이동해 연결됨.
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop");
		ws.onopen = function(){
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown", function(evt){
				if(evt.keyCode == 13){    //enter key 코드 값
					var msg = $("input[name=chatInput]").val();
					ws.send(msg);
					$("input[name=chatInput]").val("");
				}
			})
		}
		//서버에서 메세지 수신한 경우
		ws.onmessage = function(event){
			$("textarea").eq(0).prepend(event.data+"\n");
		}
		//서버와 연결 끊어진 경우
		ws.onclose = function(event){
			$("#chatStatus").text("info:connection close");
		}
	})
</script>
</head>
<body>
<p>
<div id="chatStatus"></div>
<textarea name="chatMsg" rows="15" cols="40"></textarea><br>
메세지 입력: <input type="text" name="chatInput">
</body>
</html>