package com.hogimn.myanimechart.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimeStatusNotFoundException extends RuntimeException {
    public AnimeStatusNotFoundException(int animeId) {
        super("AnimeListStatus not found for animeId: " + animeId);
    }
}