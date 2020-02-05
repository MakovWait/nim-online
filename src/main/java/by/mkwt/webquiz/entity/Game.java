package by.mkwt.webquiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Game {

    @Getter
    private Long id;

    @Setter
    @Getter
    private List<User> players;

    @Setter
    @Getter
    @JsonIgnore
    private int currentPlayerIndex = 0;

    private List<Turn> turns;

    @Getter
    @Setter
    private boolean isActive = false;

    @Getter
    @Setter
    private User winner;

    public Game(Long id) {
        this.id = id;
        this.players = new ArrayList<>();
        this.turns = new LinkedList<>();
    }

    public Game(Long id, List<User> players) {
        this.id = id;
        this.players = players;
        this.turns = new LinkedList<>();
    }

    public void newTurn(GameBoard gameBoard) {
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }

        turns.add(new Turn(gameBoard, players.get(currentPlayerIndex)));
        currentPlayerIndex++;
    }

    public Turn getLastTurn() {
        return turns.get(turns.size() - 1);
    }

}
