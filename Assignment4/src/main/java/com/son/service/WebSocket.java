package com.son.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
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
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void handleOpen(Session s, EndpointConfig config) throws Exception {
		HttpSession session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		System.out.println("session : " + session);
		if (session != null) {
			String userName = (String) session.getAttribute("userName");
			if (userName != null) {
				// save the user's name in the session for future use
				s.getUserProperties().put("userName", userName);
				System.out.println(userName + " has connected");
				
				for (Session sessions : clients) {
					System.out.println("clients : " + sessions.getId());
					sessions.getBasicRemote().sendText(userName);
				}
			}
		}
		
		if (!clients.contains(s)) {
			clients.add(s);
			// 창을 안 닫으니까 새로고침할 때마다 session id가 누적됨 (16진수임 9 > a, f > 10으로 넘어감)
			System.out.println(s.getId() + "님이 접속함");
		} else {
			System.out.println("이미 연결된 session");
		}

		// Set<Session> clients
		
	}

	@OnMessage
	public void handleMessage(Session s, String msg) throws Exception {
		System.out.println(s.getId() + "님에게 받은 메세지 : " + msg);
		boolean userOut = msg.contains("exit");
		System.err.println("msg.length() : " + msg.length());
		if (!userOut) {
			// msg encoding test(ok)
			msg = chatService.encodeMsg(msg);
		}
		
		for (Session sessions : clients) {
			System.out.println("send to clients : " + msg);
			// session에
			sessions.getBasicRemote().sendText(msg);

		}

	}

	@OnClose
	public void handleClose(Session s) {
		System.out.println(s.getId() + "님이 disconnected함");
		clients.remove(s);
	}

	@OnError
	public void handleError(Session s, Throwable t) {
		System.out.println(s.getId() + "님 에러발생");
		t.printStackTrace();
	}
}
