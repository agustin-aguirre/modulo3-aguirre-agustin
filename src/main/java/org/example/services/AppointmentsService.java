package org.example.services;

import org.example.daos.EntityDao;
import org.example.dtos.appointments.AppointmentDto;
import org.example.entities.Appointment;

public class AppointmentsService {

    private final EntityDao<Appointment, Long> repo;

    public AppointmentsService(EntityDao<Appointment, Long> repo) {
        this.repo = repo;
    }
}
