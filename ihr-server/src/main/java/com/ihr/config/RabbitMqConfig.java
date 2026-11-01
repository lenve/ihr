package com.ihr.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置：预声明框架用队列。
 * <p>框架阶段仅声明示例队列占位，业务消息（如待办通知、预警推送）后续在此扩展。</p>
 */
@Configuration
public class RabbitMqConfig {

    /** 待办/通知类消息队列（示例占位） */
    public static final String QUEUE_TODO_NOTIFY = "ihr.todo.notify.queue";

    @Bean
    public Queue todoNotifyQueue() {
        return QueueBuilder.durable(QUEUE_TODO_NOTIFY).build();
    }
}
