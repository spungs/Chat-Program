package com.son.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Component
public class WebSocketconfig {

	@Bean
	public ServerEndpointExporter see() {
		return new ServerEndpointExporter();
	}
}
