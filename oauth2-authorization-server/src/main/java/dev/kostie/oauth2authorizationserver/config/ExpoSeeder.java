package dev.kostie.oauth2authorizationserver.config;

import dev.kostie.oauth2authorizationserver.entities.Client;
import dev.kostie.oauth2authorizationserver.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Client seeder when a client does not exist in the database.
// Matches the client side information of the React Application.
@Configuration
public class ExpoSeeder {

    @Bean
    CommandLineRunner seedExpoClient(ClientRepository clientRepository) {
        return args -> {
            if (clientRepository.findByClientId("expo").isEmpty()) {
                Client expo = new Client();
                expo.setClient_id("expo");
                expo.setScope("openid");
                expo.setAuth_method("none");
                expo.setGrant_type("authorization_code");
                expo.setRedirect_uri("reactclient://redirect");
                clientRepository.save(expo);
            }
        };
    }
}
