package org.example.dtos.appointments;

import java.time.LocalDateTime;

public record MakeAppointmentDto (Integer clientId, LocalDateTime dateTime)
{ }
