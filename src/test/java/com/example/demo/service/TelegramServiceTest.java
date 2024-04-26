package com.example.demo.service;

import com.example.demo.model.telegram.custom.BotInfo;
import com.example.demo.model.telegram.custom.ChatInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
public class TelegramServiceTest {

    @Autowired
    private TelegramService telegramService;

    @Test
    public void test() {
        int round = 10;
        BotInfo bot = telegramService.getBotInfos().get(0);
        ChatInfo chat = telegramService.getChatInfos().get(0);
        for (int i = 1; i <= round; i++) {
            telegramService.sendMessage(formatMsg(String.format("test bot async #%d", i)), bot, chat);
        }
    }

    @Test
    public void testAsync() {
        int round = 10;
        BotInfo bot = telegramService.getBotInfos().get(0);
        ChatInfo chat = telegramService.getChatInfos().get(0);
        for (int i = 1; i <= round; i++) {
            telegramService.sendMessageAsync(formatMsg(String.format("test bot async #%d", i)), bot, chat);
        }
    }

    @Test
    public void testMultipleBotsInOneChet() {
        int round = 42;
        BotInfo bot01 = telegramService.getBotInfos().get(0);
        BotInfo bot02 = telegramService.getBotInfos().get(1);
        ChatInfo chat = telegramService.getChatInfos().get(0);
        for (int i = 1; i <= round; i++) {
            telegramService.sendMessageAsync(formatMsg(String.format("test bot #%d", i)), bot01, chat);
            i++;
            telegramService.sendMessageAsync(formatMsg(String.format("test bot #%d", i)), bot02, chat);
        }
    }

    @Test
    public void testOneBotSendMultipleChats() {
        int round = 25;
        BotInfo bot = telegramService.getBotInfos().get(0);
        ChatInfo chat01 = telegramService.getChatInfos().get(0);
        ChatInfo chat02 = telegramService.getChatInfos().get(1);
        for (int i = 1; i <= round; i++) {
            telegramService.sendMessageAsync(formatMsg(String.format("test bot #%d", i)), bot, chat01);
            telegramService.sendMessageAsync(formatMsg(String.format("test bot #%d", i)), bot, chat02);
        }
    }

    @Test
    public void testAsyncRateLimiter() throws InterruptedException {
        int round = 40;
        BotInfo bot = telegramService.getBotInfos().get(0);
        ChatInfo chat = telegramService.getChatInfos().get(0);
        for (int i = 1; i <= round; i++) {
            telegramService.sendMessageAsyncRateLimitAnnotation(formatMsg(String.format("test bot #%d", i)), bot, chat);
        }
    }

    private String formatMsg(String msg) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s %s", now, msg);
    }
}
