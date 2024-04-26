package com.example.demo.service;

import com.example.demo.annotation.RateLimited;
import com.example.demo.model.telegram.TelegramMsgBody;
import com.example.demo.model.telegram.custom.BotInfo;
import com.example.demo.model.telegram.custom.ChatInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "telegram")
public class TelegramService {

    private final RestTemplate restTemplate;

    @Getter
    @Setter
    private List<BotInfo> botInfos;

    @Getter
    @Setter
    private List<ChatInfo> chatInfos;

    @PostConstruct
    public void init() {
        log.info("BotInfo: {} \n ChatInfo: {}", botInfos, chatInfos);
    }

    @RateLimited(name = "tgAlert", limitRefreshPeriod = 3000, limitForPeriod = 1, timeoutDuration = 120000)
    @Async("asyncExecutor")
    public void sendMessageAsyncRateLimitAnnotation(String message, BotInfo botInfo, ChatInfo chatInfo) {
        this.doPost(message, botInfo, chatInfo);
    }

    @Async("asyncExecutor")
    public void sendMessageAsync(String message, BotInfo botInfo) {
        this.doPost(message, botInfo);
    }

    private void doPost(String message, BotInfo botInfo) {
        TelegramMsgBody body = new TelegramMsgBody();
        body.setChatId(botInfo.getChatId());
        body.setText(message);
        body.setParseMode("HTML");
//        body.setDisableNotification(false);
//        body.setDisableWebPagePreview(true);
        HttpEntity<TelegramMsgBody> entity = new HttpEntity<>(body);
        String url = "https://api.telegram.org/bot" + botInfo.getToken() + "/sendMessage";

        try {
            this.restTemplate.postForEntity(url, entity, String.class, new Object[0]);
        } catch (Exception var9) {
            log.error("[Alert] TG Alert Setting Error : Bot = {}, Exception:{}", botInfo, ExceptionUtils.getStackTrace(var9));
        }
    }

    public void sendMessage(String message, BotInfo botInfo, ChatInfo chatInfo) {
        this.doPost(message, botInfo, chatInfo);
    }

    @Async("asyncExecutor")
    public void sendMessageAsync(String message, BotInfo botInfo, ChatInfo chatInfo) {
        this.doPost(message, botInfo, chatInfo);
    }

    private void doPost(String message, BotInfo botInfo, ChatInfo chatInfo) {
        TelegramMsgBody body = new TelegramMsgBody();
        body.setChatId(chatInfo.getChatId());
        body.setText(message);
        body.setParseMode("HTML");
//        body.setDisableNotification(false);
//        body.setDisableWebPagePreview(true);
        HttpEntity<TelegramMsgBody> entity = new HttpEntity<>(body);
        String url = "https://api.telegram.org/bot" + botInfo.getToken() + "/sendMessage";

        try {
            this.restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception var9) {
            log.error("[Alert] TG Alert Setting Error : Bot = {}, Chat = {}, Exception:{}",
                    botInfo, chatInfo, ExceptionUtils.getStackTrace(var9));
        }
    }
}
