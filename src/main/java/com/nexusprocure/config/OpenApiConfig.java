package com.nexusprocure.config;

import com.nexusprocure.properties.ApiProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
@RequiredArgsConstructor
public class OpenApiConfig {
   private final ApiProperties apiProperties;
   @Bean
    public OpenAPI openAPI(){
       return new OpenAPI()
               .info(
                       new Info()
                               .title(apiProperties.getTitle())
                               .description(apiProperties.getDescription())
                               .version(apiProperties.getVersion())
                               .contact(
                                       new Contact()
                                               .name(apiProperties.getContact().getName())
                                               .email(apiProperties.getContact().getEmail())
                                               .url(apiProperties.getContact().getUrl())
                               )
                               .license(
                                       new License()
                                               .name(apiProperties.getLicense().getName())
                                               .url(apiProperties.getLicense().getName())
                               )
               )
               .components(
                       new Components()
                               .addSecuritySchemes(
                                       "bearerAuth",
                                       new SecurityScheme()
                                               .type(SecurityScheme.Type.HTTP)
                                               .scheme("bearer")
                                               .bearerFormat("JWT")
                                               .in(SecurityScheme.In.HEADER)
                               )

               )
               .addSecurityItem(
                       new SecurityRequirement()
                               .addList("bearerAuth")
               );


   }
    }

