package hello.messaging;

import hello.data.OutputMessage;
import hello.data.User;
import hello.enums.UserState;
import hello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompClientSupport;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;

@Configuration
public class MessageEventListener {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private UserRepository userRepository;

	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		if(sha.getDestination().equals("/topic/members/list")){
			messageSender.sendGlobalMessage(new OutputMessage("Server", String.format("%s Connected", HtmlUtils.htmlEscape(event.getUser().getName()))));
			messageSender.sendMemberList();
		}
	}

	@EventListener
	public void handleConnectEvent(SessionConnectEvent event) {
		User user = new User(event.getUser().getName(), UserState.ONLINE);
		userRepository.createOrUpdateUser(user);
	}

	@EventListener
	public void handleDisconnectEvent(SessionDisconnectEvent event) {
		User user = new User(event.getUser().getName(), UserState.OFFLINE);
		userRepository.createOrUpdateUser(user);
		messageSender.sendMemberList();
	}

}
