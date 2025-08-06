package org.example.dtos.appointments;

import org.example.dtos.clients.ClientDto;

import java.time.LocalDateTime;

public record AppointmentDto (Integer id, ClientDto clientDto, LocalDateTime dateTime)
{ }
