package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final CurrentLevelCalculator currentLevelCalculator;
    private final ExperienceUntilNextLevelCalculator experienceUntilNextLevelCalculator;

    public PlayerService(PlayerRepository playerRepository,
                         CurrentLevelCalculator currentLevelCalculator,
                         ExperienceUntilNextLevelCalculator experienceUntilNextLevelCalculator) {
        this.playerRepository = playerRepository;
        this.currentLevelCalculator = currentLevelCalculator;
        this.experienceUntilNextLevelCalculator = experienceUntilNextLevelCalculator;
    }

    public Player create(Player player) {
        player.setUntilNextLevel(experienceUntilNextLevelCalculator.calculate());
        player.setLevel(currentLevelCalculator.calculate());
        return playerRepository.save(player);
    }

    public Optional<Player> get(Long id) {
        return playerRepository.findById(id);
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }


}
