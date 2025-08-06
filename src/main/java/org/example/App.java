package org.example;

import org.example.connections.DbConnectionProvider;
import org.example.connections.SqlConnectionProvider;
import org.example.daos.AppointmentsRepository;
import org.example.daos.ClientsRepository;
import org.example.daos.EntityDao;
import org.example.entities.Appointment;
import org.example.entities.Client;
import org.example.services.AppointmentsService;
import org.example.services.ClientsService;


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

        ClientsService clientsService = new ClientsService(clientsRepo);
        AppointmentsService appointmentsService = new AppointmentsService(appointmentsRepo, clientsRepo);

        System.out.println(appointmentsService.getAppointmentsOfClient(1));
    }
}
