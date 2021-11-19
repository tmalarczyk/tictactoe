package pl.training.tictactoe;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UsersRepository {

    private final Map<String, String> users = new ConcurrentHashMap<>();

    public synchronized void save(String user, String socketId) {
        users.put(user, socketId);
    }

    public synchronized String getSocketId(String user) {
        return users.get(user);
    }

    public synchronized Optional<String> getUser(String socketId) {
        return users.entrySet().stream()
                .filter(entry -> entry.getValue().equals(socketId))
                .findFirst()
                .map(Map.Entry::getKey);
    }

}
