package com.game.service;

import org.springframework.http.ResponseEntity;

public class IdParser {

    public static Long parseId(String id) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
            if (idLong.equals(0L)) {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return idLong;
    }
}
