package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public Player[] getList(String name, String title, Race race, Profession profession, Long after, Long before,
                            Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                            Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize) {
        Specification<Player> specification = getAllFieldsSpecification(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel);

        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.DEFAULT_DIRECTION, order.getFieldName());
        Page<Player> playersPage = playerRepository.findAll(specification, page);
        return playersPage.getContent().toArray(new Player[0]);
    }

    public int getCount(String name, String title, Race race, Profession profession, Long after, Long before,
                        Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                        Integer maxLevel) {
        Specification<Player> specification = getAllFieldsSpecification(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel);

        return (int) playerRepository.count(specification);
    }

    public boolean delete(Long id) {
        if(playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private static Specification<Player>
    getAllFieldsSpecification(String name, String title, Race race, Profession profession, Long after, Long before,
                              Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                              Integer maxLevel) {
        final Specification<Player> trueSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and();

        return Specification
                .where(name == null ? trueSpecification : PlayerSpecifications.nameContains(name))
                .and(title == null ? trueSpecification : PlayerSpecifications.titleContains(title))
                .and(race == null ? trueSpecification : PlayerSpecifications.raceEquals(race))
                .and(profession == null ? trueSpecification : PlayerSpecifications.professionEquals(profession))
                .and(banned == null ? trueSpecification : PlayerSpecifications.bannedEquals(banned))
                .and(after == null ? trueSpecification : PlayerSpecifications.birthdayAfter(after))
                .and(before == null ? trueSpecification : PlayerSpecifications.birthdayBefore(before))
                .and(minExperience == null ? trueSpecification : PlayerSpecifications.minExperience(minExperience))
                .and(maxExperience == null ? trueSpecification : PlayerSpecifications.maxExperience(maxExperience))
                .and(minLevel == null ? trueSpecification : PlayerSpecifications.minLevel(minLevel))
                .and(maxLevel == null ? trueSpecification : PlayerSpecifications.maxLevel(maxLevel));
    }
}
