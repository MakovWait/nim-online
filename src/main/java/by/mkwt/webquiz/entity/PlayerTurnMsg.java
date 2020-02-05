package by.mkwt.webquiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerTurnMsg {

    private long gameId;
    private int row;
    private int col;

}
