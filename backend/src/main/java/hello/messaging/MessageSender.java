package hello.messaging;

import hello.data.Message;
import hello.data.OutputMessage;
import hello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class MessageSender {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRegistry sessionRegistry;

	public void sendMemberList(){
		template.convertAndSend("/topic/members/list", userRepository.getUsers());
	}

	public void sendGlobalMessage(OutputMessage outputMessage) {
		template.convertAndSend("/topic/globalMessages", outputMessage);
	}

	public void sendMessageToUser(String userId, OutputMessage outputMessage) {
		template.convertAndSendToUser(userId, "/queue", outputMessage);
	}

}
