package com.example.demo.model.telegram.custom;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BotInfo {
    private String name;
    private String token;
    private String chatId;
}
