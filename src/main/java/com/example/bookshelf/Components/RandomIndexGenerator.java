package com.example.bookshelf.Components;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomIndexGenerator {
    public int generateStartIndex(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
