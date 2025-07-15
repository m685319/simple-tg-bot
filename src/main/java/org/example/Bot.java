package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        System.out.println(message.getText());
    }
}
