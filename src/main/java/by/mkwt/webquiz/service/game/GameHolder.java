package by.mkwt.webquiz.service.game;

import by.mkwt.webquiz.dao.GameArchiveRepository;
import by.mkwt.webquiz.entity.Game;
import by.mkwt.webquiz.entity.GameArchive;
import by.mkwt.webquiz.entity.Player;
import by.mkwt.webquiz.entity.User;
import by.mkwt.webquiz.service.util.IdCounter;
import by.mkwt.webquiz.service.util.game.GameBoardRandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameHolder {

    private IdCounter idCounter;
    private Map<Long, Game> games;
    private final GameBoardRandomGenerator gameBoardGenerator;
    private final GameArchiveRepository gameArchiveRepository;

    @Autowired
    public GameHolder(IdCounter idCounter, GameBoardRandomGenerator gameBoardGenerator, GameArchiveRepository gameArchiveRepository) {
        this.idCounter = idCounter;
        this.gameBoardGenerator = gameBoardGenerator;
        this.gameArchiveRepository = gameArchiveRepository;
        games = new HashMap<>();
    }

    public Game createGame(List<User> players) {
        Long id = idCounter.createID();
        Game game = new Game(id, players);
        game.setActive(true);
        game.newTurn(gameBoardGenerator.getRandomGameBoard());
        games.put(id, game);

        return game;
    }

    public Game getGameById(Long id) {
        return games.get(id);
    }

    public Game getActiveGameByPlayer(User user) {
        for (Game game : games.values()) {
            if (game.isActive() && game.getPlayers().contains(user)) {
                return game;
            }
        }
        return null;
    }

    public GameArchive save(Game game) {
        GameArchive gameArchive = new GameArchive();
        Set<Player> players = new HashSet<>();

        for (User user : game.getPlayers()) {
            players.add(new Player(user, user.equals(game.getWinner())));
        }

        gameArchive.setPlayers(players);

        gameArchiveRepository.save(gameArchive);
        games.remove(game.getId());
        return gameArchive;
    }

    public List<GameArchive> getArchiveGames() {
        return gameArchiveRepository.findAll();
    }

    public List<GameArchive> getArchiveGamesByUsername(String username) {
        return gameArchiveRepository.findAllByUsername(username);
    }

}
