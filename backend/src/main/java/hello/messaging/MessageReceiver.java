package hello.messaging;

import hello.data.Message;
import hello.data.OutputMessage;
import hello.data.User;
import hello.enums.UserState;
import hello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageReceiver {

    @Autowired
    private MessageSender messageSender;

    @MessageMapping("/sendMessage")
    public void receiveMessage(Message message, Principal principal) {
        final OutputMessage outputMessage = new OutputMessage(principal.getName(), message.getMessage());
        messageSender.sendGlobalMessage(outputMessage);
    }

    @MessageMapping("/queue/user/{userId}")
    public void receivePrivateMessage(Message message, @DestinationVariable String userId, Principal principal) {
        final OutputMessage outputMessage = new OutputMessage(principal.getName(), message.getMessage());
        messageSender.sendMessageToUser(userId, outputMessage);
    }

}
