package com.son.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Component 어노테이션이 달린 클래스는 스프링 빈에 등록되고 그 인스턴스는 
 * 싱글톤으로 스프링에 의해 관리되지만, @ServereEndPoint로 어노테이션이 달린 클래스는
 * WebSocket이 연결될 때마다 인스턴스가 생성되고 JWA구현에 의해 관리가 되어 내부의
 * @Autowired가 설정된 멤버가 정상적으로 초기화 되지 않는다.
 * @Autowired를 사용하기 위해서 ServerEndpointConfig.Configurator를 사용하여
 * ServerEndPoint의 컨텍스트에 BeanFactory 또는 ApplicationContext를 연결해주는
 * 작업을 하는 클래스를 다음과 같이 생성한다.
 * @author KyoungHo Son
 */
@Configuration
public class ServerEndpointConfigurator extends javax.websocket.server.ServerEndpointConfig.Configurator implements ApplicationContextAware{

	private static ApplicationContext context;
	
	@Override
	public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
		return context.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ServerEndpointConfigurator.context = applicationContext;
	}
	
	@Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }

}
