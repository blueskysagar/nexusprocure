package com.nexusprocure.properties;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact = new Contact();
    private License license = new License();
    @Getter
    @Setter
    public static class Contact{
        private String name;
        private String email;
        private String url;
    }
    @Getter
    @Setter
    public static class License{
        private String name;
        private String url;
    }
}
