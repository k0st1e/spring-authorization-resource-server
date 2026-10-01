package dev.kostie.oauth2authorizationserver.services;

import dev.kostie.oauth2authorizationserver.entities.Client;
import dev.kostie.oauth2authorizationserver.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class CustomClientService implements RegisteredClientRepository {

    final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(Client.from(registeredClient));
    }

    @Override
    public @Nullable RegisteredClient findById(String id) {
        var client = clientRepository.findById(Integer.valueOf(id)).orElseThrow();
        return Client.from(client);
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String client_id) {
        var client = clientRepository.findByClientId(client_id).orElseThrow();
        return Client.from(client);
    }
}
