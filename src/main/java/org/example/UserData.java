package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    private int score;
    private int current;

    public void incrementCurrent() {
        current++;
    }

    public void incrementScore() {
        score++;
    }

    public void reset() {
        current = 0;
        score = 0;
    }
}