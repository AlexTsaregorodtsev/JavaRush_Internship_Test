package com.game.service;

import com.game.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FieldsDataManager {

    private static String[] methods = {"Name", "Title", "Race", "Profession", "Experience", "Birthday"};
    public static void checkData(Player player) throws IllegalArgumentException {

        if(player.getName().equals("")) {
            throw new IllegalArgumentException();
        }

        if(player.getName().length() > 12) {
            throw new IllegalArgumentException();
        }

        if(player.getTitle().length() > 30) {
            throw new IllegalArgumentException();
        }

        if(player.getBirthday().getYear() < (2000 - 1900) || player.getBirthday().getYear() > (3000 - 1900)) {
            throw new IllegalArgumentException();
        }

        if(player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            throw new IllegalArgumentException();
        }
    }

    public static List<String> checkNull(Player player) {
        List<String> nullFields = new ArrayList<>();
        Class clas = player.getClass();
        Object result;
        try {
            for(String methodName : methods) {
                Method method = clas.getMethod("get" + methodName, null);
                result = method.invoke(player, null);
                if(result == null) {
                    nullFields.add(methodName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nullFields;
    }

    public static Player setNullParams(Player inputPlayer, Player outdatedPlayer,
                                     List<String> toSetField) {
        Class clas = inputPlayer.getClass();
        try {
            for(String methodName : toSetField) {
                Method methodGet = clas.getMethod("get" + methodName, null);
                Method methodSet = clas.getMethod("set" + methodName, methodGet.getReturnType());
                Object outdatedData = methodGet.invoke(outdatedPlayer, null);
                methodSet.invoke(inputPlayer, outdatedData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputPlayer;
    }
}
