package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;
import java.text.MessageFormat;
import java.util.Date;

public class PlayerSpecifications {

    private PlayerSpecifications() {
    }

    public static Specification<Player> nameContains(String expression) {
        return (root, query, builder) -> builder.like(root.get("name"), contains(expression));
    }

    public static Specification<Player> titleContains(String expression) {
        return (root, query, builder) -> builder.like(root.get("title"), contains(expression));
    }

    public static Specification<Player> raceEquals(Race race) {
        return (root, query, builder) -> builder.equal(root.get("race"), race);
    }

    public static Specification<Player> professionEquals(Profession profession) {
        return (root, query, builder) -> builder.equal(root.get("profession"), profession);
    }

    public static Specification<Player> birthdayAfter(Long after) {
        Date date = new Date(after);
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("birthday"), date);
    }

    public static Specification<Player> birthdayBefore(Long before) {
        Date date = new Date(before);
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("birthday"), date);

    }

    public static Specification<Player> bannedEquals(Boolean banned) {
        return (root, query, builder) -> builder.equal(root.get("banned"), banned);
    }

    public static Specification<Player> minExperience (Integer experience) {
        return ((root, query, builder) -> builder.ge(root.get("experience"), experience));
    }

    public static Specification<Player> maxExperience (Integer experience) {
        return (root, query, builder) -> builder.le(root.get("experience"), experience);
    }

    public static Specification<Player> minLevel (Integer level) {
        return ((root, query, builder) -> builder.ge(root.get("level"), level));
    }

    public static Specification<Player> maxLevel (Integer level) {
        return (root, query, builder) -> builder.le(root.get("level"), level);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
