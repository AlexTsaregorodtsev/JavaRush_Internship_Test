package com.game.service;

import org.springframework.stereotype.Component;


public class ExperienceUntilNextLevelCalculator {
    public static Integer calculate(Integer experience, Integer level) {
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        return untilNextLevel;
    }
}
