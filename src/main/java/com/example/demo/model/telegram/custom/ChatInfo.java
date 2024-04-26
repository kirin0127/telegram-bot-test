package com.example.demo.model.telegram.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatInfo {
    private String name;
    private String chatId;
}
