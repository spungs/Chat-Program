<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat Page</title>
</head>
<body>
	<form>
		<!-- 송신 메세지를 작성하는 텍스트 박스 -->
		<input id="textMessage" type="text">
		<!-- 메세지 송신을 하는 버튼 -->
		<input value="전송" onclick="sendMessage()" type="button">
		<!-- webSocket 접속 종료하는 버튼 -->
		<input value="나가기" onclick="exit()" type="button">
	</form>
	<br>
	<textarea id="messageTextArea" rows="10" cols="50"></textarea>
	
	<script type="text/javascript">
		var webSocket = new WebSocket("ws://localhost/ws?userName=${userName}&roomName=${roomName}");
		
		var messageTextArea = document.getElementById("messageTextArea");
		
		webSocket.onopen = function(message) {
			console.log("onopen 실행");
			console.log(message);
			messageTextArea.value += "${userName}님이 접속하였습니다..\n";
		};
		
		webSocket.onclose = function(message) {
			messageTextArea.value += "소켓 나감..\n";
		};
		
		webSocket.onerror = function(message) {
			messageTextArea.value += "에러발생..\n";
		};
		
		webSocket.onmessage = function(message) {
			var messageData = message.data;
			if (messageData.includes("exit")) {
				messageTextArea.value += "전송받은 메세지(js) : " + messageData.substring(7) + "\n";
			} else {
				messageTextArea.value += "전송받은 메세지(js) : " + messageData + "\n";
			}
		};
		
		function sendMessage() {
			var message = document.getElementById("textMessage");
			// 웹소켓이 연결 끊기면 전송 안되도록
			// messageTextArea.value += "서버에게 보낸 메세지 : " + message.value + "\n";		
			
			webSocket.send(message.value);
			
			message.value = "";
		}
		
		function disconnect() {
			webSocket.close();
		}
		
		function exit() {
			console.log("${userName}");
			webSocket.send("exit : ${userName}");
			disconnect();
			location.href='roomList';
		}
	</script>
</body>
</html>