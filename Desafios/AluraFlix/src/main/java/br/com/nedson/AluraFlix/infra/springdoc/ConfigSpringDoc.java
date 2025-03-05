package br.com.nedson.AluraFlix.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigSpringDoc {

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
                        .title("AluraFlix API")
                        .description("API Rest do desafio Alura Flix, contendo as funcionalidades de CRUD de vídeos e de categorias e autenticação via JWT")
                        .contact(new Contact()
                                .name("Nedson Vieira")
                                .url("https://www.linkedin.com/in/nedson-vieira/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://aluraflix/api/licenca")));
    }
}
