package com.ihr.module.platform.dto;

/**
 * 发送消息请求体。
 */
public record SendMessageRequest(String receiverName, String title, String content) {
}
