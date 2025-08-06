package org.example.services.clients;

import org.example.dtos.clients.ClientDto;
import org.example.dtos.clients.RegisterClientDto;

public interface ClientsService {
    ClientDto registerClient(RegisterClientDto registerClientDto);

    void updateClient(Integer id, ClientDto updatedClientDto);

    ClientDto getClientWithId(Integer id);
}
