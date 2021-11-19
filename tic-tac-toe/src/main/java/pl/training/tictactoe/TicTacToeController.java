package pl.training.tictactoe;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;

@Controller
@Log
@RequiredArgsConstructor
public class TicTacToeController {

    private final UsersRepository usersRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void onMessage(@Payload Turn message, @Header("simpSessionId") String sessionId) {
        message.setTimestamp(LocalTime.now());
        log.info("New message: " + message + " from " + sessionId);
        if (message.getRecipient().equals("all")) {
            messagingTemplate.convertAndSend("/main-room/messages", message);
        } else {
            var recipientSocketId = usersRepository.getSocketId(message.getRecipient());
            messagingTemplate.convertAndSend("/private-room/user" + recipientSocketId, message);
            messagingTemplate.convertAndSend("/private-room/user" + recipientSocketId, message);
        }
    }


}
