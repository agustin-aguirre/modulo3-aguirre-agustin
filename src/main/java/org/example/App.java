package org.example;

import org.example.connections.DbConnectionProvider;
import org.example.connections.SqlConnectionProvider;
import org.example.daos.AppointmentsRepository;
import org.example.daos.ClientsRepository;
import org.example.daos.EntityDao;
import org.example.entities.Appointment;
import org.example.entities.Client;
import org.example.services.appointments.AppointmentsServiceImpl;
import org.example.services.clients.ClientsServiceImpl;


public class App
{
    public static void main( String[] args )
    {
        SqlConnectionProvider connProvider = new DbConnectionProvider(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
        EntityDao<Client, Integer> clientsRepo = new ClientsRepository(connProvider);
        EntityDao<Appointment, Integer> appointmentsRepo = new AppointmentsRepository(connProvider);

        ClientsServiceImpl clientsService = new ClientsServiceImpl(clientsRepo);
        AppointmentsServiceImpl appointmentsService = new AppointmentsServiceImpl(appointmentsRepo, clientsRepo);


//        var newClientDto = clientsService.registerClient(new RegisterClientDto("Juan Perez"));
//        clientsService.updateClient(newClientDto.id(), new ClientDto(newClientDto.id(), "Juansito Perezito"));
        System.out.println(clientsService.getClientWithId(9));


        // appointmentsService.makeAppointment(new MakeAppointmentDto(newClientDto.id(), LocalDateTime.of(2025, 8, 8, 15, 0)));
        // System.out.println(appointmentsService.getAppointmentsOfClient(newClientDto.id()));
    }
}
