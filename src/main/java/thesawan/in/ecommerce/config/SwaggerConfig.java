package thesawan.in.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce App APIs")
                        .description("REST APIs for E-commerce Platform - Built by Sawan")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Sawan Kumar")
                                .email("sawankushwaha222@gmail.com")
                                .url("https://thesawan.in"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://ecommerce-app-1-q5hv.onrender.com").description("Production Server")
                ));
    }
}
