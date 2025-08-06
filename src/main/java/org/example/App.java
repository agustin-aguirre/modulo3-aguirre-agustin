package org.example;

import org.example.connections.DbConnectionProvider;
import org.example.connections.SqlConnectionProvider;
import org.example.daos.AppointmentsRepository;
import org.example.daos.DoctorsRepository;
import org.example.daos.EntityDao;
import org.example.entities.Appointment;
import org.example.entities.Doctor;

public class App
{
    public static void main( String[] args )
    {
        SqlConnectionProvider connProvider = new DbConnectionProvider(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
        EntityDao<Doctor, Long> doctorsRepo = new DoctorsRepository(connProvider);
        EntityDao<Appointment, Long> appointmentsRepo = new AppointmentsRepository(connProvider);

        System.out.println(doctorsRepo.getAll());
    }
}
