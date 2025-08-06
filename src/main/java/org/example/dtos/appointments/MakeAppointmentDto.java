package org.example.dtos.appointments;

import java.time.LocalDateTime;

public record MakeAppointmentDto (Long doctorId, LocalDateTime dateTime, String pacientName)
{ }
