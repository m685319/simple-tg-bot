package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "JavaQuizzerBot";
    }

    @Override
    public String getBotToken() {
        return "REDACTED_BOT_TOKEN";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        System.out.println(message.getText());
    }
}
