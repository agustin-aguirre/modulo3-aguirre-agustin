package org.example.dtos.appointments;

import org.example.dtos.doctors.DoctorDto;

import java.time.LocalDateTime;

public record AppointmentDto (Long id, DoctorDto doctorDto, LocalDateTime dateTime, String pacientName)
{ }
