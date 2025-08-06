package org.example.services;

import org.example.daos.EntityDao;
import org.example.daos.exceptions.PersistenceException;
import org.example.dtos.clients.ClientDto;
import org.example.dtos.clients.RegisterClientDto;
import org.example.entities.Client;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ClientsService {

    private final EntityDao<Client, Integer> repo;

    public ClientsService(EntityDao<Client, Integer> repo) {
        this.repo = repo;
    }


    public ClientDto registerClient(RegisterClientDto registerClientDto) {
        // El nombre del cliente no puede estar vacío o ser null
        if (registerClientDto.name().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Client newClient = new Client();
        // Se le quitan los espacios excesivos
        newClient.setName(registerClientDto.name().trim());
        try {
            newClient = repo.add(newClient);
        } catch (PersistenceException e) {
            throw e;
        }
        // se retorna un DTO
        return new ClientDto(newClient.getId(), newClient.getName());
    }

    public void updateClient(Integer id, ClientDto updatedClientDto) {
        if (!updatedClientDto.id().equals(id)) {
            throw new IllegalArgumentException("Ids dont match");
        }
        // El nombre del cliente no puede estar vacío o ser null
        if (repo.get(id).isEmpty()) {
            throw new NoSuchElementException();
        }
        if (updatedClientDto.name().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Client newClient = new Client(
            id,
            updatedClientDto.name().trim() // Se le quitan los espacios excesivos
        );
        try {
            repo.update(newClient);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    public ClientDto getClientWithId(Integer id) {
        Optional<Client> target = repo.get(id);
        if (target.isEmpty()) {
            throw new NoSuchElementException();
        }
        // se retorna un DTO
        return new ClientDto(id, target.get().getName());
    }
}
