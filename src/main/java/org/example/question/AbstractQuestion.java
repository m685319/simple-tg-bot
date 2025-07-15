package org.example.question;

import lombok.Getter;

@Getter
public abstract class AbstractQuestion {
    private String question;
    private String correctAnswer;

    public AbstractQuestion(String question) {
        this.question = question;
    }

    public abstract boolean checkAnswer(String answer);
}
