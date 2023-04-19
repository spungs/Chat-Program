<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat Room List</title>
<script type="text/javascript">
	// msg가 있다면 메세지 창 띄우기
	var msg = "${msg}";
	if (msg == 1 && "${roomName}" != "") {
		alert("'${roomName}'이/가 삭제되었습니다.");
	} else if ("${msg}" != ""){
		alert("${msg}");
	}
	// GET 파라미터 제거 후 새로고침 
	history.replaceState({}, null, "roomList");
	
	function newRoom() {
		var roomNameObj = document.getElementById('roomName');
		var userName = document.getElementById('userName');
		var roomNameValue = roomNameObj.value.replace(/\s+/g, '');
		if (roomNameValue == ''){ // 입력한게 없으면 return
			roomNameObj.value = '';
			return;
		}
		addRoom(roomNameObj, userName);
		location.reload();
	}
	
	function addRoom(roomNameObj, userName) {
		var req = new XMLHttpRequest();
		req.onreadystatechange = function() {
			if (req.readyState == 4 && req.status == 200) {
				var res = JSON.parse(req.responseText);
				var roomList = document.getElementById('roomList');
				
				if (res.checkAdd == '1'){
					roomList.innerHTML += "<option id='"+ res.roomName +"' ondblclick=location.href='chatDoor?roomName="+ res.roomName + "'>" + res.roomName + "</option>";
				} else {
					alert(res.roomName + '은/는 이미 존재하는 방입니다.');
				}
			}
		}
		req.open('post', 'addList');
		
		var reqData = {
				"roomName" : roomNameObj.value,
				"userName" : userName.value
		};
		var JsonReqData = JSON.stringify(reqData);
// 		console.log(JsonReqData);
		req.setRequestHeader('Content-Type', 'application/json');
		req.send(JsonReqData);
	}
	
	function selectRoom(e) {
		var input = document.getElementById('selectRoom');
		input.value = e.value;
	}
	
	function deleteRoom() {
		var frm = document.getElementById('frm');
		var roomNameObj = document.getElementById('roomList');
		var roomName = roomNameObj.value;
		
		if (roomName != ''){
			var deleteYN = confirm("'" + roomName + "' 채팅방을 삭제하시겠습니까?");
			if (deleteYN) {
				frm.method = 'post';
				frm.action = 'deleteRoom';
				frm.submit();
			}
		}
	}
</script>
</head>
<body>
	<span>현재 이름 : ${userName}</span>
	<button id="mvChatApp" onclick="location.href='chatApp'" type="button">이름 변경</button>
	<br><br>
	<form>
		<input id="roomName" placeholder="방 이름 입력" type="text" required="required">
		<input value="새로운 방 만들기" onclick="newRoom()" type="button">
	</form>
	<br>
	<div>
		<select id="roomList" multiple="multiple" style="width: 200px; height: 150px">
			<c:forEach items="${roomList }" var="room">
				<option id="${room}" onclick="selectRoom(this)" ondblclick="location.href='chat?userName=${userName}&roomName=${room}'">${room}</option>
			</c:forEach>
		</select>
	</div>
	<form id="frm">
		<input type="hidden" id="userName" name="userName" value="${userName}">
		<input type="hidden" id="selectRoom" name="roomName">
		<button id="deleteBtn" onclick="deleteRoom()" type="button">방 삭제하기</button>
	</form>
	
</body>
<script type="text/javascript">
	const inputRoomName = document.getElementById('roomName');
	const submitBtn = document.querySelector("input[type='button']");
	
	inputRoomName.addEventListener("keydown", function(e) {
		if (e.key === "Enter") {
			e.preventDefault();
			submitBtn.click();
		}
	});
	
</script>
</html>