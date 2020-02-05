package by.mkwt.webquiz.controller;

import by.mkwt.webquiz.service.game.GameHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/games")
@RestController
public class GameArchiveController {

    private final GameHolder gameHolder;

    @Autowired
    public GameArchiveController(GameHolder gameHolder) {
        this.gameHolder = gameHolder;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String userName) {
        if (userName != null) {
            return ResponseEntity.ok(gameHolder.getArchiveGamesByUsername(userName));
        }

        return ResponseEntity.ok(gameHolder.getArchiveGames());
    }

}
