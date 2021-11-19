package pl.training.tictactoe;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class SessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final UsersRepository usersRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        var headers = event.getMessage().getHeaders();
        var sessionId = headers.get("simpSessionId").toString();
        usersRepository.getUser(sessionId)
                .ifPresent(user -> messagingTemplate.convertAndSend("/main-room/messages", createSystemMessage("User "  +  user + " is now disconnected from chat")));
    }

    private Turn createSystemMessage(String text) {
        var message = new Turn();
        message.setTimestamp(LocalTime.now());
        message.setSender("System");
        message.setText(text);
        return message;
    }

}
