package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player create(Player player) throws IllegalArgumentException {
        if (FieldsDataManager.checkNull(player).size() > 0) {
            throw new IllegalArgumentException();
        }
        FieldsDataManager.checkData(player);
        player.setLevel(CurrentLevelCalculator.calculate(player.getExperience()));
        player.setUntilNextLevel(ExperienceUntilNextLevelCalculator.
                calculate(player.getExperience(), player.getLevel()));
        if (player.getBanned() == null) {
            player.setBanned(false);
        }
        return playerRepository.save(player);
    }

    public Player update(Player inputPlayer, Player outDatedPlayer) throws IllegalArgumentException {
        List<String> methodsToCall = FieldsDataManager.checkNull(inputPlayer);
        Player resultPlayer = FieldsDataManager.
                setNullParams(inputPlayer, outDatedPlayer, methodsToCall);
        FieldsDataManager.checkData(resultPlayer);
        resultPlayer.setLevel(CurrentLevelCalculator.calculate(resultPlayer.getExperience()));
        resultPlayer.setUntilNextLevel(ExperienceUntilNextLevelCalculator.
                calculate(resultPlayer.getExperience(), resultPlayer.getLevel()));
        if (resultPlayer.getBanned() == null) {
            resultPlayer.setBanned(outDatedPlayer.getBanned());
        }
        return playerRepository.save(resultPlayer);
    }

    public Optional<Player> getById(Long id) {
        return playerRepository.findById(id);
    }

    public Player[] getList(String name, Integer page) {
        List<Player> list = playerRepository.findByNameContaining(name, PageRequest.of(page, 3, Sort.by(PlayerOrder.ID.getFieldName())));
        Player[] playerArray = new Player[list.size()];
        for (int i = 0; i < list.size(); i++) {
            playerArray[i] = list.get(i);
        }
        return playerArray;
    }

    public boolean delete(Long id) {
        if(playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
