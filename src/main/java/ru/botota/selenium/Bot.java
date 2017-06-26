package ru.botota.selenium;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mazh0416 on 6/26/2017.
 */
public class Bot extends TelegramLongPollingBot {
    private long chatId = 183375382L;;

    public void onUpdateReceived(Update update) {
        //chatId = update.getMessage().getChatId();
        sendToTelegram("ok");
    }

    public void sendToTelegram(String text){
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendToTelegram(String name, InputStream image){
        try {
            sendPhoto(new SendPhoto().setNewPhoto(name, image).setChatId(chatId));
            image.close();
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }


    public String getBotUsername() {
        return "test";
    }

    public String getBotToken() {
        return "207572767:AAGIfFfsL5DIC6vRpAtzb1pkEgAXgi3ESVM";
    }
}
