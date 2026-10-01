package dev.kostie.oauth2authorizationserver.repositories;

import dev.kostie.oauth2authorizationserver.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("""
    SELECT c from Client c WHERE c.client_id = :client_id
    """)
    Optional<Client> findByClientId(String client_id);
}
