package com.game.service;

import org.springframework.stereotype.Component;


public class CurrentLevelCalculator {
    public static Integer calculate(Integer experience) {
        Integer level = (int) ((Math.sqrt(2500 + 200*experience) - 50)  / 100);
        return level;
    }
}
