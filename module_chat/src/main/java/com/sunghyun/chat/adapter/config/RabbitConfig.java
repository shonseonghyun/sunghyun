package com.sunghyun.chat.adapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {
//    private final RabbitProperties rabbitProperties;
//
//    @Value("${spring.rabbitmq.host}")
//    String RABBITMQ_HOST;
//
//    // 4가지 Binding 전략 중 TopicExchange 전략을 사용. "chat.exchange"를 이름으로 지정
//    @Bean
//    public TopicExchange chatExchange() {
//        return new TopicExchange(rabbitProperties.getExchange().getName());
//    }
//
//    //QUEUE 설정
//    @Bean
//    public Queue chatQueue() {
//        return new Queue(rabbitProperties.getQueue().getChat(), true); // durable을 true로 제공
//    }
//    @Bean
//    public Queue memberQueue() {
//        return new Queue(rabbitProperties.getQueue().getMember(), true); // durable을 true로 제공
//    }
//
//    // BINDING 설정
//    @Bean
//    public Binding chatBinding(Queue chatQueue, TopicExchange chatExchange) {
//        // chatExchange 이름으로 전달된 메시지 중,
//        // 라우팅 키와 패턴이 매칭되는 큐로 메시지가 라우팅된다.
//        return BindingBuilder
//                .bind(chatQueue)
//                .to(chatExchange)
//                .with(rabbitProperties.getRouting().getChat().getKey());
//    }
//
//    @Bean
//    public Binding memberBinding(Queue memberQueue, TopicExchange chatExchange) {
//        return BindingBuilder
//                .bind(memberQueue)
//                .to(chatExchange)
//                .with(rabbitProperties.getRouting().getMember().getKey());
//    }

    // RabbitMQ와 메시지 담당할 클래스
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // JSON 형식의 메시지를 직렬화하고 역직렬할 수 있도록 설정
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        return new RabbitMessagingTemplate(rabbitTemplate);
    }

    // RabbitMQ와 연결 설정. CachingConnectionFactory를 선택
//    @Bean
//    public ConnectionFactory createConnectionFactory() {
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost(RABBITMQ_HOST);
//        factory.setUsername("guest"); // RabbitMQ 관리자 아이디
//        factory.setPassword("guest"); // RabbitMQ 관리자 비밀번호
//        factory.setPort(5672); // RabbitMQ 연결할 port
//        factory.setVirtualHost("/"); // vhost 지정
//
//        return factory;
//    }


    // 메시지를 JSON으로 직렬/역직렬화
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}