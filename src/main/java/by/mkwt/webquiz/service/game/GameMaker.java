package by.mkwt.webquiz.service.game;

import by.mkwt.webquiz.entity.Game;
import by.mkwt.webquiz.entity.User;
import by.mkwt.webquiz.service.util.IdCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameMaker {

    private List<User> freeUsers;
    private final SimpMessageSendingOperations messagingTemplate;
    private final IdCounter idCounter;
    private final GameHolder gameHolder;

    @Autowired
    public GameMaker(SimpMessageSendingOperations messagingTemplate, IdCounter idCounter, GameHolder gameHolder) {
        this.messagingTemplate = messagingTemplate;
        this.idCounter = idCounter;
        this.gameHolder = gameHolder;
        freeUsers = new ArrayList<>();
    }

    public synchronized void addFreeUser(User user) {
        if (!freeUsers.contains(user)) {
            freeUsers.add(user);
        }

        if (freeUsers.size() >= 2) {
            List<User> players = new LinkedList<>();

            for (int i = 0; i < 2; i++) {
                players.add(freeUsers.remove(0));
            }

            Game game = gameHolder.createGame(players);

            for (User player : players) {
                messagingTemplate.convertAndSendToUser(player.getUsername(), "/secured/topic/games", game);
            }
        }
    }

    public void removeFreeUser(User user) {
        freeUsers.remove(user);
    }

    public List<User> getFreeUsers() {
        return freeUsers;
    }
}
