package hello.messaging;

import hello.data.Message;
import hello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private UserRepository userRepository;

	public void sendMemberList(){
		template.convertAndSend("/topic/members/list", userRepository.getUsers());
	}

	public <T extends Message> void sendGlobalMessage(T message) {
		template.convertAndSend("/topic/globalMessages", message);
	}

	public void sendMessageToUser(String userId, String message) {
		template.convertAndSendToUser(userId, "/queue", message);
	}

}
