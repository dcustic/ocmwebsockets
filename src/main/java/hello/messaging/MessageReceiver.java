package hello.messaging;

import hello.data.Message;
import hello.data.User;
import hello.enums.UserState;
import hello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageReceiver {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private UserRepository userRepository;


    @MessageMapping("/join")
    public void receiveJoin(String username) throws Exception {
        User user = new User(username, UserState.ONLINE);
        userRepository.createOrUpdateUser(user);

        messageSender.sendGlobalMessage(new Message("Server", String.format("%s Connected", HtmlUtils.htmlEscape(username))));
        messageSender.sendMemberList();
    }

    @MessageMapping("/disconnect")
    public void receiveDisconnect(String username) throws Exception {
        User user = new User(username, UserState.OFFLINE);
        userRepository.createOrUpdateUser(user);
        messageSender.sendMemberList();
    }

    @MessageMapping("/sendMessage")
    public <T extends Message> void receiveMessage(T message) {
        messageSender.sendGlobalMessage(message);
    }

}
