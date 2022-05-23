package com.game.controller;

import com.game.entity.Player;
import com.game.service.IdParser;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@Controller
@RequestMapping("/rest/players")
public class PlayerController {

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<Player> create(@RequestBody Player player) {
//        Player responseBody = playerService.create(player);
//        return new ResponseEntity<Player>(responseBody, HttpStatus.OK);

        try {
            return new ResponseEntity<>(playerService.create(player), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> update(@RequestBody Player player, @PathVariable String id) {
        try {
            Long idLong = IdParser.parseId(id);

            Optional<Player> playerOpt = playerService.getById(idLong);
            if (!playerOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            player.setId(idLong);
            return new ResponseEntity<>(playerService.update(player, playerOpt.get()), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getById(@PathVariable String id) {
        try {
            Optional<Player> playerOpt = playerService.getById(IdParser.parseId(id));
            if (playerOpt.isPresent()) {
                return new ResponseEntity<>(playerOpt.get(), HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Player[]> getList
            (@RequestParam(required = false) String name, @RequestParam(required = false) Integer pageNumber) {
            return new ResponseEntity<>(playerService.getList(name, pageNumber), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> delete(@PathVariable String id) {
        try {
            if(playerService.delete(IdParser.parseId(id))) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
