package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@Controller
@RequestMapping("/rest/players/")
public class PlayerController {

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    private final PlayerService playerService;

    @PostMapping
    public Player create(@RequestBody Player player) {
//        Player responseBody = playerService.create(player);
//        return new ResponseEntity<Player>(responseBody, HttpStatus.OK);
        return playerService.create(player);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> get(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            if (idLong.equals(0L)) {
                return ResponseEntity.badRequest().build();
            }
            Optional<Player> playerOpt = playerService.get(idLong);
            if (playerOpt.isPresent()) {
                return new ResponseEntity<>(playerOpt.get(), HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Player> delete(@PathVariable String id) {
//        try {
//            Long idLong = Long.parseLong(id);
//            if (idLong.equals(0L)) {
//                return ResponseEntity.badRequest().build();
//            }
//            Optional<Player> playerOpt = playerService.get(idLong);
//            if (playerOpt.isPresent()) {
//                return new ResponseEntity.ok().b
//            }
//            return ResponseEntity.notFound().build();
//        } catch (NumberFormatException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
