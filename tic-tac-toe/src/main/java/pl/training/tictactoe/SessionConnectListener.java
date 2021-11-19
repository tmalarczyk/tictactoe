package pl.training.tictactoe;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SessionConnectListener implements ApplicationListener<SessionConnectedEvent> {

    private final UsersRepository usersRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        var headers = event.getMessage().getHeaders();
        var sessionId = headers.get("simpSessionId").toString();
        var nativeHeaders = (Map<String, List<String>>) ((GenericMessage)headers.get("simpConnectMessage")).getHeaders().get("nativeHeaders");
        var user = nativeHeaders.get("user").get(0);
        usersRepository.save(user, sessionId);
        messagingTemplate.convertAndSend("/main-room/messages", createSystemMessage("User " + user + " is now connected to chat"));
    }

    private Turn createSystemMessage(String text) {
        var message = new Turn();
        message.setTimestamp(LocalTime.now());
        message.setSender("System");
        message.setText(text);
        return message;
    }

}
