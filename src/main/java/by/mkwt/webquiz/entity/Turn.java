package by.mkwt.webquiz.entity;

import lombok.*;

@Data
public class Turn {

    private GameBoard gameBoard;
    private User currentPlayer;

    public Turn(User currentPlayer) {
        this.gameBoard = new GameBoard();
        this.currentPlayer = currentPlayer;
    }

    public Turn(GameBoard gameBoard, User currentPlayer) {
        this.gameBoard = gameBoard;
        this.currentPlayer = currentPlayer;
    }
}
