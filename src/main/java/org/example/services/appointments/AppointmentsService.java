package org.example.services.appointments;

import org.example.dtos.appointments.AppointmentDto;
import org.example.dtos.appointments.MakeAppointmentDto;

import java.util.Collection;

public interface AppointmentsService {
    AppointmentDto makeAppointment(MakeAppointmentDto makeAppointmentDto);

    void cancelAppointment(Integer appointmentId);

    Collection<AppointmentDto> getAppointmentsOfClient(Integer clientId);
}
