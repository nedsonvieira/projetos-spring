package br.com.nv_food.avaliacao.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvaliacaoAMQPConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    public Queue filaDetalhesAvaliacao() {
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-avaliacao")
                .deadLetterExchange("pagamentos.dlx")
                .build();
    }

    @Bean
    public Queue filaDlqDetalhesAvaliacao() {
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-avaliacao-dlq")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("pagamentos.ex")
                .build();
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return ExchangeBuilder
                .fanoutExchange("pagamentos.dlx")
                .build();
    }

    @Bean
    public Binding bindPagamentoPedido() {
        return BindingBuilder
                .bind(filaDetalhesAvaliacao())
                .to(fanoutExchange());
    }

    @Bean
    public Binding bindDlxPagamentoPedido() {
        return BindingBuilder
                .bind(filaDlqDetalhesAvaliacao())
                .to(deadLetterExchange());
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory connection) {
        return new RabbitAdmin(connection);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
