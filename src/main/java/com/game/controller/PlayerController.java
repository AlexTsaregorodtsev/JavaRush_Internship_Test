package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
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
            return playerOpt
                    .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Player[]> getList(
            @RequestParam(required = false) String name, @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race, @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,  @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,  @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,  @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
            return new ResponseEntity<>(playerService.getList(name, title, race, profession, after, before, banned,
                    minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount(
            @RequestParam(required = false) String name, @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race, @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,  @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,  @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,  @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel) {
        return new ResponseEntity<>(playerService.getCount(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel), HttpStatus.OK);
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
