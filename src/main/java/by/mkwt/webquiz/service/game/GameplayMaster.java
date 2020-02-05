package by.mkwt.webquiz.service.game;

import by.mkwt.webquiz.entity.Game;
import by.mkwt.webquiz.entity.GameBoard;
import by.mkwt.webquiz.entity.PlayerTurnMsg;
import by.mkwt.webquiz.entity.User;
import by.mkwt.webquiz.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameplayMaster {

    private final GameHolder gameHolder;
    private final UserService userService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public GameplayMaster(GameHolder gameHolder, UserService userService, SimpMessageSendingOperations messagingTemplate) {
        this.gameHolder = gameHolder;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    public void handlePlayerTurn(User user, PlayerTurnMsg playerTurnMsg) {
        Game game = gameHolder.getGameById(playerTurnMsg.getGameId());

        if (game.isActive() && game.getPlayers().contains(user)) {
            if (user.getUsername().equals(game.getLastTurn().getCurrentPlayer().getUsername())) {
                List<Integer> board = new ArrayList<>(game.getLastTurn().getGameBoard().getBoard());

                if (playerTurnMsg.getRow() >= 0 && playerTurnMsg.getRow() < board.size()) {
                    if (playerTurnMsg.getCol() >= 0 && playerTurnMsg.getCol() < board.get(playerTurnMsg.getRow())) {
                        board.set(playerTurnMsg.getRow(), playerTurnMsg.getCol());
                    }
                }

                game.newTurn(new GameBoard(board));

                if (game.getLastTurn().getGameBoard().isBoardValuesEmpty()) {
                    setWinner(game, game.getLastTurn().getCurrentPlayer());
                }

                for (User player : game.getPlayers()) {
                    messagingTemplate.convertAndSendToUser(player.getUsername(), "/secured/topic/games", game);
                }
            }
        }
    }

    public void handlePlayerDisconnect(User user) {
        Game game = gameHolder.getActiveGameByPlayer(user);
        if (game != null && game.isActive()) {
            for (User player : game.getPlayers()) {
                if (!player.getUsername().equals(user.getUsername())) {
                    setWinner(game, player);
                }
            }

            for (User player : game.getPlayers()) {
                messagingTemplate.convertAndSendToUser(player.getUsername(), "/secured/topic/games", game);
            }
        }
    }

    private void setWinner(Game game, User user) {
        if (game.isActive()) {
            game.setActive(false);
            game.setWinner(user);

            user.setScore(user.getScore() + 1);
            userService.save(user);
            gameHolder.save(game);
        }
    }
}
