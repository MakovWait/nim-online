package by.mkwt.webquiz.service.util.game;

import by.mkwt.webquiz.entity.GameBoard;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameBoardRandomGenerator {

    private final Random random = new Random();

    public GameBoard getRandomGameBoard() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.getBoard().add(random.nextInt(6) + 1);
        gameBoard.getBoard().add(random.nextInt(6) + 1);
        gameBoard.getBoard().add(random.nextInt(6) + 1);
        gameBoard.getBoard().add(random.nextInt(6) + 1);

        return gameBoard;
    }

}
