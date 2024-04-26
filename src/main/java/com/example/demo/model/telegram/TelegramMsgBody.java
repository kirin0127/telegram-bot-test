package com.example.demo.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramMsgBody {
    private String text;

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("parse_mode")
    private String parseMode;
}
