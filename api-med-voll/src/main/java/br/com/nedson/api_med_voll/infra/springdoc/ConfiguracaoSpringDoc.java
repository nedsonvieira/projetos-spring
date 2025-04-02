package br.com.nedson.api_med_voll.infra.springdoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoSpringDoc {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().
                                        type(SecurityScheme.Type.HTTP).
                                        scheme("bearer").
                                        bearerFormat("JWT")))
                .info(new Info()
                        .title("Voll.med API")
                        .description("API Rest da aplicação Med.voll, contendo as funcionalidades de CRUD de médicos e de pacientes, além de agendamento e cancelamento de consultas")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("backend@med.voll"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://med.voll/api/licenca")));
    }
}
