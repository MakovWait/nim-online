package by.mkwt.webquiz.controller;

import by.mkwt.webquiz.entity.PlayerTurnMsg;
import by.mkwt.webquiz.entity.User;
import by.mkwt.webquiz.service.game.GameplayMaster;
import by.mkwt.webquiz.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GamePlayController {

    private final GameplayMaster gameplayMaster;
    private final UserService userService;

    @Autowired
    public GamePlayController(GameplayMaster gameplayMaster, UserService userService) {
        this.gameplayMaster = gameplayMaster;
        this.userService = userService;
    }

    @MessageMapping("/turns/")
    @SendTo("/topic/games/")
    public void newPlayerTurnHandle(Principal principal, PlayerTurnMsg playerTurnMsg) {
        User player = userService.findByUsername(principal.getName());
        gameplayMaster.handlePlayerTurn(player, playerTurnMsg);
    }

}

