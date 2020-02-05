package by.mkwt.webquiz.service.event;

import by.mkwt.webquiz.entity.User;
import by.mkwt.webquiz.service.game.GameMaker;
import by.mkwt.webquiz.service.game.GameplayMaster;
import by.mkwt.webquiz.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private final UserService userService;
    private final GameMaker gameMaker;
    private final GameplayMaster gameplayMaster;

    @Autowired
    public WebSocketEventListener(UserService userService, GameMaker gameMaker, GameplayMaster gameplayMaster) {
        this.userService = userService;
        this.gameMaker = gameMaker;
        this.gameplayMaster = gameplayMaster;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {
        final String subId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSubscriptionId();
        final String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        User user = userService.findByUsername(Objects.requireNonNull(event.getUser()).getName());
        gameMaker.addFreeUser(user);
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        final String subId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSubscriptionId();
        User user = userService.findByUsername(Objects.requireNonNull(event.getUser()).getName());
        gameplayMaster.handlePlayerDisconnect(user);
    }

    @EventListener
    public void sessionDisconnectHandler(SessionDisconnectEvent event) {
        User user = userService.findByUsername(Objects.requireNonNull(event.getUser()).getName());
        gameplayMaster.handlePlayerDisconnect(user);
    }
}
