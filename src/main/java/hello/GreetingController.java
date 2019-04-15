package hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class GreetingController {


    @MessageMapping("/join")
    @SendTo("/topic/members")
    public Greeting join(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }


    @MessageMapping("/sendMessage")
    @SendTo("/topic/globalMessages")
    public <T extends Message> T sendGlobalMessage(T message) {
        return message;
    }

}
