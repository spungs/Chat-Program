<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat Page</title>
<style type="text/css">
/*
.message-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.balloon {
  background-image: url("balloon.png");
  background-size: contain;
  background-repeat: no-repeat;
  background-position: right bottom;
  padding: 10px 15px;
  border-radius: 20px;
  display: inline-block;
}

#textMessage {
  height: 50px;
  width: 100%;
  max-width: 80%;
  padding: 10px;
  box-sizing: border-box;
  border: none;
  border-radius: 10px;
  resize: none;
}
*/
.chat-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
}

.chat {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow-y: auto;
}

.message {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  margin-bottom: 10px;
}

.bubble {
  padding: 10px;
  border-radius: 20px;
}

.left .bubble {
  background-color: #f1f0f0;
}

.right .bubble {
  background-color: #cef2ff;
}

.left .time {
  margin-left: 10px;
  font-size: 12px;
}

.right .time {
  margin-right: 10px;
  font-size: 12px;
}

.text {
  display: block;
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.5;
  max-width: 200px;
}

</style>
</head>
<body>

<div class="chat-wrapper">
  <div class="chat">
    <div class="message left">
      <div class="bubble">
        <span class="text">상대방이 보낸 메세지</span>
      </div>
      <span class="time">오전 11:30</span>
    </div>
    <div class="message right">
      <div class="bubble">
        <span class="text">내가 보낸 메세지</span>
      </div>
      <span class="time">오후 1:20</span>
    </div>
    <div class="message left">
      <div class="bubble">
        <span class="text">상대방이 보낸 메세지</span>
      </div>
      <span class="time">오후 2:00</span>
    </div>
    <div class="message right">
      <div class="bubble">
        <span class="text">내가 보낸 메세지</span>
      </div>
      <span class="time">오후 3:15</span>
    </div>
  </div>
</div>

	
	<div class="wrapper">
		<form>
			<!-- 송신 메세지를 작성하는 텍스트 박스 -->
			<input id="textMessage" type="text" placeholder="Enter a message">
			<!-- 메세지 송신을 하는 버튼 -->
			<input value="전송" onclick="sendMessage()" type="button">
			<!-- webSocket 접속 종료하는 버튼 -->
			<input value="나가기" onclick="exit()" type="button">
		</form>
		<br>
		<textarea id="messageTextArea" rows="10" cols="50"></textarea>
	</div>
	
	
	<script type="text/javascript">
		var webSocket = new WebSocket("ws://localhost/ws?userName=${userName}&roomName=${roomName}");
		
		var messageTextArea = document.getElementById("messageTextArea");
		
		webSocket.onopen = function(message) {
// 			console.log("onopen 실행");
// 			console.log(message);
// 			messageTextArea.value += "${userName}님이 접속하였습니다..\n";
		};
		
		webSocket.onclose = function(message) {
			messageTextArea.value += "소켓 나감..\n";
		};
		
		webSocket.onerror = function(message) {
			messageTextArea.value += "에러발생..\n";
		};
		
		webSocket.onmessage = function(message) {
			var msgData = message.data;
			var idx = msgData.indexOf(':');
// 			console.log("idx : " + idx);	
// 			console.log("msg : "+msgData);
			if (msgData.includes("exit ${roomName}")) {
				messageTextArea.value += msgData.substring(idx + 1) + "님이 퇴장하셨습니다.\n";
			} else if (msgData.includes("connect")){
				messageTextArea.value += msgData.substring(idx + 1) + "님이 입장하셨습니다.\n";
			} else if (msgData.includes("msg")) {
				var index = msgData.indexOf(';');
				messageTextArea.value += "전송받은 메세지 : " + msgData.substring(index + 1) + "\n";
			}
		};
		
		function sendMessage() {
			var message = document.getElementById("textMessage");
			// 웹소켓이 연결 끊기면 전송 안되도록 
			//  ㄴ끊기면 전송안되는듯
			// messageTextArea.value += "서버에게 보낸 메세지 : " + message.value + "\n";		
			
			webSocket.send(message.value);
			
			message.value = "";
			message.focus();
		}
		
		function disconnect() {
			webSocket.close();
		}
		
		function exit() {
// 			console.log("${userName}");
			webSocket.send("exit ${roomName}:${userName}");
			disconnect();
			location.href='roomList';
		}
		
	</script>
</body>
</html>