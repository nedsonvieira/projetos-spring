package br.com.nedson.nv_ecommerce.produtos_service.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

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
                        .title("API de Produtos-Service da NV e-Commerce")
                        .description("Microsserviço de gerenciamento de produtos")
                        .contact(new Contact()
                                .name("Nedson Vieira")
                                .url("https://www.linkedin.com/in/nedson-vieira/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://nv-e-commerce/api/licenca")));
    }
}