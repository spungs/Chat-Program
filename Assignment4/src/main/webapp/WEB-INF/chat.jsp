<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat Page</title>
<style type="text/css">
body {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 500px;
}

.title {
  display: flex;
  justify-content: center;
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
}

.chat-wrapper {
	display: flex;
	flex-direction: column;
	height: 300px;
	width: 500px;
	padding: 10px;
	align-items: flex-end;
	background: #D3D3D3;
}

.chat {
	display: flex;
	flex-direction: column;
	flex-grow: 1;
	width: 500px;
	overflow-y: auto;
}

.title {
	width: 500px;
 	display: flex;
 	justify-content: center;
}

.message {
	display: flex;
	flex-direction: row;
	align-items: flex-end;
	margin-bottom: 10px;
}

/* right class 우측 정렬 */
.message.right {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 10px;
}

/* left class 좌측 정렬 */
.message.left {
	align-self: flex-start;
	border-radius: 10px;
	padding: 10px;
	margin-bottom: 10px;
	max-width: 60%;
}

.center {
	display: flex;
	justify-content: center;
	font-size: 12px;
}


.bubble {
	padding: 10px;
	border-radius: 20px;
}

.left .bubble {
	background-color: white;
}

.right .bubble {
	background-color: #cef2ff;
}

.center .bubble {
	background-color: #D3D3D3;
	padding: 10px;
	border-radius: 20px;
	font-size: 12px;
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

.wrapper {
  display: flex;
  flex-direction: column;
  height: 80px;
  margin-top: 10px;
}

form {
  display: flex;
  align-items: center;
  height: 100%;
}

.input-group {
  flex: 1;
  display: flex;
  margin-right: 10px;
}

.form-control {
  flex: 1;
  border-radius: 0;
  border: none;
  border-bottom: 1px solid gray;
  margin-right: 10px;
}

.submitBtn, .decodeBtn {
  border: none;
  border-radius: 5px;
  background-color: #cef2ff;
  color: #333;
  padding: 8px 16px;
  cursor: pointer;
}

.submitBtn:hover, .decodeBtn {
  background-color: #a3e1ff;
}

.header-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
}

.exitBtn {
  background-color: #333;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
      position: absolute;
    right: 0;
}
</style>
</head>
<body>
	<div class="header-wrapper">
	  <div class="title">${roomName}</div>
	  <button class="exitBtn" onclick="exit()" type="button">나가기</button>
	</div>

	<div class="chat-wrapper">
	  <div id="chat-content" class="chat">
	  </div>
	</div>

	<div class="wrapper">
	  <form>
	    <div class="input-group">
	      <input id="sendMsg" type="text" class="form-control">
	      <div class="input-group-append">
	        <button class="submitBtn" onclick="sendMessage()" type="button">전송</button>
	        <button class="decodeBtn" onclick="decoding()" type="button">디코딩</button>
	      </div>
	    </div>
	  </form>
	</div>
	
	<script type="text/javascript">
		var webSocket = new WebSocket("ws://localhost/ws?userName=${userName}&roomName=${roomName}");
		var chatContent = document.getElementById("chat-content");
		
		// chat 클래스 요소에 노드가 추가될 때마다 이벤트 발생
		chatContent.addEventListener("DOMNodeInserted", function(event) { 
			// 스크롤을 하단으로 내려줌
			chatContent.scrollTop = chatContent.scrollHeight; 
		});
		
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
			console.log("idx : " + idx);	
			console.log("msg : " + msgData);
			if (msgData.includes("exit ${roomName}")) {
				chatContent.innerHTML += "<div class=\"message center\">"
			      						+ 	"<div class=\"bubble\">"
		        						+ 		"<span class=\"text\">" + msgData.substring(idx + 1) + "님이 퇴장하셨습니다.</span>"
		     							+ 	"</div>"
		    							+ "</div>";
			} else if (msgData.includes("connect")){
				chatContent.innerHTML += "<div class=\"message center\">"
										+ 	"<div class=\"bubble\">"
										+ 		"<span class=\"text\">" + msgData.substring(idx + 1) + "님이 입장하셨습니다.</span>"
										+ 	"</div>"
										+ "</div>";
			} else if (msgData.includes("msg")) {
				console.log(msgData); // msg;edge:H
				var index = msgData.indexOf(';');
				var content = msgData.substring(idx + 1);
				var user = msgData.substring(index + 1, idx);
				if (user == "${userName}") {
					chatContent.innerHTML += "" 
											+ "<div class=\"message right\">"
											+ 	"<div class=\"bubble\">"
											+ 		"<span class=\"text content\">" + content + "</span>"
											+ 	"</div>"
											+ "</div>";
				} else {
					chatContent.innerHTML += "" 
											+ "<div class=\"message left\">"
											+ 	"<div class=\"bubble\">"
											+ 		"<span class=\"text content\">" + user + " : " + content + "</span>"
											+ 	"</div>"
											+ "</div>";
				}
			}
		};
		
		function sendMessage() {
			var message = document.getElementById("sendMsg");
			// 웹소켓이 연결 끊기면 전송 안되도록 
			//  ㄴ끊기면 전송안되는듯
			// messageTextArea.value += "서버에게 보낸 메세지 : " + message.value + "\n";		
			
			// 입력값이 없으면 return 하도록
			if (message.value == "") {
				return;
			}
			
			webSocket.send(message.value);
			
			message.value = "";
			message.focus();
		}
		
		function disconnect() {
			webSocket.close();
		}
		
		function exit() {
			disconnect();
			location.href='roomList';
		}
		
		function decoding() {
			
		}
		
	</script>
</body>
</html>