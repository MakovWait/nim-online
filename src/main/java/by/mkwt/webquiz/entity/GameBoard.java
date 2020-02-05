package by.mkwt.webquiz.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GameBoard {

    private List<Integer> board;

    public GameBoard() {
        board = new ArrayList<>();
    }

    public boolean isBoardValuesEmpty() {
        for (Integer val : board) {
            if (val > 0) {
                return false;
            }
        }
        return true;
    }
}
