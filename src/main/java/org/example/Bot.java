package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final Dotenv dotenv;

    public Bot() {
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
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
        Message message = update.getMessage();
        String text = message.getText();
        Long userId = message.getFrom().getId();
        if (text.equals("/start")) {
            sendText(userId, "👋 Hi! I'm JavvyBot. Want to test your Java knowledge? Type /quiz to begin!");
        } else if (text.equals("/quiz")) {
            sendText(userId, "🧠 Quiz time! Question 1: What does the `==` operator do when comparing objects?");
        } else {
            sendText(userId, "Unknown command. Try /quiz or /start.");
        }
        System.out.println(message.getText());
    }
}
