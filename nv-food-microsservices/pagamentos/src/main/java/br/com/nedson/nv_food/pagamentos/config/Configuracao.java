package br.com.nedson.nv_food.pagamentos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuracao {

    @Bean
    public ModelMapper criarModelMapper(){
        return new ModelMapper();
    }
}
