<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat Room List</title>
</head>
<body>
	<form id="frm" action="roomList" method="post">
		<input id="userName" name="userName" placeholder="이름 입력" type="text" required="required">
		<input value="App 시작하기" type="submit">
	</form>
</body>
<script type="text/javascript">
	//msg가 있다면 메세지 창 띄우기
	var msg = '${msg}';
	if (msg != 0) {
		alert('${msg}');
	}
	
	const inputUserName = document.getElementById('userName');
	const submitBtn = document.querySelector("input[type='submit']");
	
	inputUserName.addEventListener("keydown", function(e) {
		if (e.key === "Enter") {
			submitBtn.click();
		}
	});
</script>
</html>