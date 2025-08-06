package org.example.services.appointments;

import org.example.daos.EntityDao;
import org.example.daos.exceptions.PersistenceException;
import org.example.dtos.appointments.AppointmentDto;
import org.example.dtos.appointments.MakeAppointmentDto;
import org.example.dtos.clients.ClientDto;
import org.example.entities.Appointment;
import org.example.entities.Client;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class AppointmentsServiceImpl implements AppointmentsService {

    private final EntityDao<Appointment, Integer> repo;
    private final EntityDao<Client, Integer> clientsRepo;

    public AppointmentsServiceImpl(EntityDao<Appointment, Integer> repo, EntityDao<Client, Integer> clientsRepo) {
        this.repo = repo;
        this.clientsRepo = clientsRepo;
    }

    @Override
    public AppointmentDto makeAppointment(MakeAppointmentDto makeAppointmentDto) {
        // El cliente debe estar registrado
        Optional<Client> targetClient = clientsRepo.get(makeAppointmentDto.clientId());
        if (targetClient.isEmpty()) {
            throw new NoSuchElementException(); // client not found
        }

        // El appointment debe ser en día de semana (Lunes a Viernes)
        DayOfWeek dayOfWeek = makeAppointmentDto.dateTime().getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException();
        }

        // El appointment debe ser en horario laboral (09:00 a 17:00)
        LocalTime hour = makeAppointmentDto.dateTime().toLocalTime();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);
        if (hour.isBefore(start) || hour.isAfter(end)) {
            throw new IllegalArgumentException();
        }

        Appointment newAppointment = new Appointment();
        newAppointment.setClientId(makeAppointmentDto.clientId());
        newAppointment.setDateTime(makeAppointmentDto.dateTime());

        try {
            newAppointment = repo.add(newAppointment);
        }
        catch (PersistenceException e) {
            throw e;
        }

        ClientDto clientDto = new ClientDto(targetClient.get().getId(), targetClient.get().getName());

        // se retorna un dto
        return new AppointmentDto(newAppointment.getId(), clientDto, newAppointment.getDateTime());
    }

    @Override
    public void cancelAppointment(Integer appointmentId) {
        Optional<Appointment> target = repo.get(appointmentId);
        // tiene que existir
        if (target.isEmpty()) {
            throw new NoSuchElementException();
        }
        // tiene que ser en fecha futura
        if (LocalDateTime.now().isAfter(target.get().getDateTime())) {
            throw new IllegalArgumentException();
        }

        try {
            repo.delete(appointmentId);
        }
        catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    public Collection<AppointmentDto> getAppointmentsOfClient(Integer clientId) {
        // el cliente debe estar registrado
        Optional<Client> target = clientsRepo.get(clientId);
        if (target.isEmpty()) {
            throw new NoSuchElementException();
        }

        // filtrado y mapeo todo en uno
        ClientDto clientDto = new ClientDto(clientId, target.get().getName());
        return repo.getAll().stream()
                .filter(appointment -> appointment.getClientId().equals(clientId))
                .map(appointment -> new AppointmentDto(appointment.getId(), clientDto, appointment.getDateTime()))
                .toList();
    }
}
