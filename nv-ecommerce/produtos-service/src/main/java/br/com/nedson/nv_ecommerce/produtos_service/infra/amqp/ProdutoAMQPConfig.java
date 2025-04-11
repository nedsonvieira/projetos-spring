package br.com.nedson.nv_ecommerce.produtos_service.infra.amqp;

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
public class ProdutoAMQPConfig {

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory connection) {
        return new RabbitAdmin(connection);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

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
    public Queue filaProdutoCadastrado() {
        return QueueBuilder
                .nonDurable("produto.cadastrado")
                .build();
    }

    @Bean
    public Queue filaProdutoAtualizado() {
        return QueueBuilder
                .nonDurable("produto.atualizado")
                .build();
    }

    @Bean
    public Queue filaProdutoDeletado() {
        return QueueBuilder
                .nonDurable("produto.deletado")
                .build();
    }

    @Bean
    public Binding bindFilaProdutoCadastrado(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(filaProdutoCadastrado())
                .to(topicExchange)
                .with("produto.cadastrado");
    }

    @Bean
    public Binding bindFilaProdutoAtualizado(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(filaProdutoAtualizado())
                .to(topicExchange)
                .with("produto.atualizado");
    }

    @Bean
    public Binding bindFilaProdutoDeletado(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(filaProdutoDeletado())
                .to(topicExchange)
                .with("produto.deletado");
    }

    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder
                .topicExchange("produtos.exchange")
                .build();
    }
}
