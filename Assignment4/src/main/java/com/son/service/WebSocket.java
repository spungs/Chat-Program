package com.son.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.son.config.ServerEndpointConfigurator;

@Service
@ServerEndpoint(value = "/ws", configurator = ServerEndpointConfigurator.class)
public class WebSocket {
	@Autowired
	chatServiceImpl chatService;
	
	// WebSocket Class는 client가 접속할 때마다 생성되어 client와 직접 통신하는 클래스이다.
	// 따라서 new client가 접속할 때마다 client의 세션 관련 정보를 정적형으로 저장하여 1:N의 통신이 가능하도록 만들어야 한다.
	private Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	// key(client) : value(roomName) Map으로 해야함
	private Map<String, String> clientsMap = Collections.synchronizedMap(new HashMap<String, String>());

	@OnOpen
	public void handleOpen(Session s, EndpointConfig config) throws Exception {
		HttpSession session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		String roomName = getRoomNameFromWebSocketUrl(s.getRequestURI().getQuery());
		// test pring (ok)
		System.out.println("HttpSession : " + session);
		System.out.println("roomName : " + roomName);
		
		if (session == null) {
			return;
		}
		
		String userName = (String) session.getAttribute("userName");
		
		if (userName == null) {
			return;
		}
		// save the user's name in the session for future use
		s.getUserProperties().put("userName", userName);
		System.out.println(userName + " has connected");
		// map에 userName, roomName put
//		clientsMap.put("userName", userName);
//		clientsMap.put("roomName", roomName);
//		System.out.println(clientsMap);
		
		if (!clients.contains(s)) {
			clients.add(s);
			// 창을 안 닫으니까 새로고침할 때마다 session id가 누적됨 (16진수임 9 > a, f > 10으로 넘어감)
			System.out.println(s.getUserProperties().get("userName") + "님이 접속함");
		} else {
			// 새로고침시 webSocket이 새로 열리기 때문에 사실상 무의미
			System.out.println("이미 연결된 session");
		}
		
		clientsMap.put((String) s.getUserProperties().get("userName"), roomName);
		for (Session clients : clients) {
			// map에 userName, roomName put
			System.out.println("clientsMap : " + clientsMap);
			System.out.println("value : " + clientsMap.get(userName));
			
			String clientName = (String) clients.getUserProperties().get("userName");
			if (roomName.equals(clientsMap.get(clientName))) {
				System.out.println("clients : " + clients.getUserProperties().get("userName"));
				clients.getBasicRemote().sendText("connect:" + userName);
			}
		}
	}

	private String getRoomNameFromWebSocketUrl(String query) {
		String[] params = query.split("&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 2 && keyValue[0].equals("roomName")) {
				return keyValue[1];
			}
		}
		return null;
	}

	@OnMessage
	public void handleMessage(Session s, String msg) throws Exception {
		String roomName = getRoomNameFromWebSocketUrl(s.getRequestURI().getQuery());
		String userName = (String) s.getUserProperties().get("userName");
		System.out.println(userName + "님에게 받은 메세지 : " + msg);
		boolean userOut = msg.contains("exit");
		System.out.println("msg.length() : " + msg.length());
		if (!userOut) {
			// msg encoding test(ok)
			msg = chatService.encodeMsg(msg);
		}
		for (Session clients : clients) {
			// map에 userName, roomName put
//			clientsMap.put((String) clients.getUserProperties().get("userName"), roomName);
			System.out.println("clientsMap : " + clientsMap);
			
//			System.out.println("roomName eq: " + roomName);
//			System.out.println("userName eq: " + userName);
//			System.out.println("clientsMap.get(userName) eq: " + clientsMap.get(userName));
			String clientName = (String) clients.getUserProperties().get("userName");
			System.out.println("클라이언트 이름 : "+clientName);
			if (roomName.equals(clientsMap.get(clientName))) {
				System.out.println("send to clients : " + msg);
				clients.getBasicRemote().sendText("msg;" + msg);
			}
		}
		
//		for (Session sessions : clients) {
//			// session에
//			sessions.getBasicRemote().sendText(msg);
//		}
		
	}

	@OnClose
	public void handleClose(Session s) {
		System.out.println(s.getUserProperties().get("userName") + "님이 disconnected함");
		clients.remove(s);
	}

	@OnError
	public void handleError(Session s, Throwable t) {
		System.out.println(s.getUserProperties().get("userName") + "님 에러발생");
		t.printStackTrace();
	}
}
