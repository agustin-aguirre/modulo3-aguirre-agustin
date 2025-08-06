package org.example.controllers;

import org.example.dtos.ApiResponse;
import org.example.dtos.EmptyApiResponse;
import org.example.dtos.appointments.AppointmentDto;
import org.example.dtos.appointments.MakeAppointmentDto;
import org.example.services.appointments.AppointmentsService;

import java.util.Collection;
import java.util.NoSuchElementException;

public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    public ApiResponse<AppointmentDto> makeAppointment(MakeAppointmentDto makeAppointmentDto) {
        try {
            var newAppointment = appointmentsService.makeAppointment(makeAppointmentDto);
            return new ApiResponse<>(201, newAppointment, null);
        }
        catch (IllegalArgumentException e) {
            return new ApiResponse<>(400, null, e.getMessage());
        }
        catch (NoSuchElementException e) {
            return new ApiResponse<>(404, null, e.getMessage());
        }
        catch (Exception e) {
            return new ApiResponse<>(500, null, e.getMessage());
        }
    }

    public EmptyApiResponse cancelAppointment(int appointmentId) {
        try {
            appointmentsService.cancelAppointment(appointmentId);
            return new EmptyApiResponse(200, null);
        }
        catch (IllegalArgumentException e) {
            return new EmptyApiResponse(400, e.getMessage());
        }
        catch (NoSuchElementException e) {
            return new EmptyApiResponse(404, e.getMessage());
        }
        catch (Exception e) {
            return new EmptyApiResponse(500, e.getMessage());
        }
    }

    public ApiResponse<Collection<AppointmentDto>> getAppointmentsFromClient(int clientId) {
        try {
            return new ApiResponse<>(200, appointmentsService.getAppointmentsOfClient(clientId), null);
        }
        catch (NoSuchElementException e) {
            return new ApiResponse<>(404, null, e.getMessage());
        }
        catch (Exception e) {
            return new ApiResponse<>(500, null, e.getMessage());
        }
    }
}
