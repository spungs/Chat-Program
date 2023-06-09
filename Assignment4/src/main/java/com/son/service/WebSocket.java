package com.son.service;

import java.io.IOException;
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
	ChatServiceImpl chatService;
	
	// WebSocket Class는 client가 접속할 때마다 생성되어 client와 직접 통신하는 클래스이다.
	// 따라서 new client가 접속할 때마다 client의 세션 관련 정보를 정적형으로 저장하여 1:N의 통신이 가능하도록 만들어야 한다.
	private Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	// key(client) : value(roomName) Map으로 해야함
	private Map<String, String> clientsMap = Collections.synchronizedMap(new HashMap<String, String>());

	@OnOpen
	public void handleOpen(Session session, EndpointConfig config) throws Exception {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		String roomName = getRoomNameFromWebSocketUrl(session.getRequestURI().getQuery());
		// test print (ok)
//		System.out.println("HttpSession : " + session);
//		System.out.println("roomName : " + roomName);
		
		if (httpSession == null) {
			return;
		}
		
		String userName = (String) httpSession.getAttribute("userName");
		
		if (userName == null) {
			return;
		}
		// save the user's name in the session for future use
		session.getUserProperties().put("userName", userName);
		System.out.println(userName + " has connected");
		// map에 userName, roomName put
//		clientsMap.put("userName", userName);
//		clientsMap.put("roomName", roomName);
//		System.out.println(clientsMap);
		
		if (!clients.contains(session)) {
			clients.add(session);
			// 창을 안 닫으니까 새로고침할 때마다 session id가 누적됨 (16진수임 9 > a, f > 10으로 넘어감)
			System.out.println(session.getUserProperties().get("userName") + "님이 접속함");
		} else {
			// 새로고침시 webSocket이 새로 열리기 때문에 사실상 무의미
			System.out.println("이미 연결된 session");
		}
		
		clientsMap.put((String) session.getUserProperties().get("userName"), roomName);
		for (Session client : clients) {
			// map에 userName, roomName put
			System.out.println("clientsMap : " + clientsMap);
			System.out.println("value : " + clientsMap.get(userName));
			
			String clientName = (String) client.getUserProperties().get("userName");
			if (roomName.equals(clientsMap.get(clientName))) {
				System.out.println("clients : " + client.getUserProperties().get("userName"));
				client.getBasicRemote().sendText("connect:" + userName);
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
	public void handleMessage(Session session, String msg) throws Exception {
		String roomName = getRoomNameFromWebSocketUrl(session.getRequestURI().getQuery());
		String userName = (String) session.getUserProperties().get("userName");
		System.out.println(userName + "님에게 받은 메세지 : " + msg);
		boolean userOut = msg.contains("exit");
		System.out.println("msg.length() : " + msg.length());
		if (!userOut) {
			// msg encoding test(ok)
			msg = chatService.encryptMsg(msg);
		}
		
		for (Session client : clients) {
			// map에 userName, roomName put
//			clientsMap.put((String) clients.getUserProperties().get("userName"), roomName);
			System.out.println("clientsMap : " + clientsMap);
			
//			System.out.println("roomName eq: " + roomName);
//			System.out.println("userName eq: " + userName);
//			System.out.println("clientsMap.get(userName) eq: " + clientsMap.get(userName));
			String clientName = (String) client.getUserProperties().get("userName");
			System.out.println("클라이언트 이름 : "+clientName);
			if (roomName.equals(clientsMap.get(clientName))) {
				System.out.println("send to clients : user:" + userName + ", " + msg);
				client.getBasicRemote().sendText("msg;" + userName + ":" + msg);
			}
		}
		
//		for (Session sessions : clients) {
//			// session에
//			sessions.getBasicRemote().sendText(msg);
//		}
	}

	@OnClose
	public void handleClose(Session s) {
		String roomName = getRoomNameFromWebSocketUrl(s.getRequestURI().getQuery());
		String userName = (String) s.getUserProperties().get("userName");
		clients.remove(s);
		for (Session client : clients) {
			String clientName = (String) client.getUserProperties().get("userName");
			if (roomName.equals(clientsMap.get(clientName))) {
				try {
					client.getBasicRemote().sendText("exit " + roomName + ":" + userName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@OnError
	public void handleError(Session s, Throwable t) {
		System.out.println(s.getUserProperties().get("userName") + "님 에러발생");
		t.printStackTrace();
	}
}
