package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.question.AbstractQuestion;
import org.example.question.GitQuestion;
import org.example.question.JavaQuestion;
import org.example.question.SQLQuestion;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ConcurrentHashMap;

public class Bot extends TelegramLongPollingBot {
    private final Dotenv dotenv;
    private final AbstractQuestion[] questions;
    private ConcurrentHashMap<Long, UserData> users;

    public Bot() {
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        questions = new AbstractQuestion[3];
        questions[0] = new JavaQuestion();
        questions[1] = new SQLQuestion();
        questions[2] = new GitQuestion();
        users = new ConcurrentHashMap<>();
    }

    @Override
    public String getBotUsername() {
        return "JavaQuizzerBot";
    }

    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        Message message = update.getMessage();
        String text = message.getText();
        Long userId = message.getFrom().getId();
        users.putIfAbsent(userId, new UserData());
        UserData data = users.get(userId);

        switch (text) {
            case "/start" :
                data.reset();
                sendText(userId, "👋 Hi! I'm JavvyBot. Want to test your Java knowledge? Type /quiz to begin!");
                break;
            case "/quiz" :
                data.reset();
                sendText(userId, "🧠 Quiz begins!");
                sendText(userId, questions[0].getQuestion());
                break;
            default :
                int current = data.getCurrent();

                if (current >= questions.length) {
                    sendText(userId, "🎉 Quiz finished! Your score: " + data.getScore() + "/" + questions.length);
                    return;
                }

                boolean result = questions[current].checkAnswer(text);
                if (result) {
                    data.incrementScore();
                    sendText(userId, "✅ Correct!");
                } else {
                    sendText(userId, "❌ Incorrect. Correct answer: " + questions[current].getCorrectAnswer());
                }

                data.incrementCurrent();

                if (data.getCurrent() < questions.length) {
                    sendText(userId, questions[data.getCurrent()].getQuestion());
                } else {
                    sendText(userId, "🎉 Quiz finished! Your score: " + data.getScore() + "/" + questions.length);
                }
        }

        System.out.println("[" + userId + "] " + text);
    }
}
