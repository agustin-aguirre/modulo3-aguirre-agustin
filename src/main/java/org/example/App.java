package org.example;

import org.example.connections.DbConnectionProvider;
import org.example.connections.SqlConnectionProvider;
import org.example.controllers.AppointmentsController;
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
        run();
    }


    public static void run() {
        SqlConnectionProvider connProvider = new DbConnectionProvider(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
        EntityDao<Client, Integer> clientsRepo = new ClientsRepository(connProvider);
        EntityDao<Appointment, Integer> appointmentsRepo = new AppointmentsRepository(connProvider);
        AppointmentsServiceImpl appointmentsService = new AppointmentsServiceImpl(appointmentsRepo, clientsRepo);
        AppointmentsController controller = new AppointmentsController(appointmentsService);
    }
}
