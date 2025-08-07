package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.dtos.AppResponse;
import org.example.dtos.EmptyAppResponse;
import org.example.dtos.appointments.AppointmentDto;
import org.example.dtos.appointments.MakeAppointmentDto;
import org.example.services.appointments.AppointmentsService;

import java.util.Collection;
import java.util.NoSuchElementException;

@Tag(name = "Appointments", description = "Appointment related operations")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }


    @Operation(
            summary = "Makes an appointments",
            description = "Makes an appointment for a register user within working hours and week days. Returns the created appointment data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The created appointement data.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Some data sent is not valid."),
                    @ApiResponse(responseCode = "404", description = "The client is not registered."),
                    @ApiResponse(responseCode = "500", description = "Some unknown error happened.")
            }
    )
    public AppResponse<AppointmentDto> makeAppointment(
            @Parameter(description = "The minimum necesary data to make an appointment.")
            MakeAppointmentDto makeAppointmentDto
    ) {
        try {
            var newAppointment = appointmentsService.makeAppointment(makeAppointmentDto);
            return new AppResponse<>(201, newAppointment, null);
        }
        catch (IllegalArgumentException e) {
            return new AppResponse<>(400, null, e.getMessage());
        }
        catch (NoSuchElementException e) {
            return new AppResponse<>(404, null, e.getMessage());
        }
        catch (Exception e) {
            return new AppResponse<>(500, null, e.getMessage());
        }
    }

    @Operation(
            summary = "Cancels an appointment",
            description = "Cancels a future appointment, given its id.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "The appointment was successfully cancelled.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyAppResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Attempted to cancel a past appointment."),
                    @ApiResponse(responseCode = "404", description = "The appointement wasn't found."),
                    @ApiResponse(responseCode = "500", description = "Some unknown error happened.")
            }
    )
    public EmptyAppResponse cancelAppointment(
            @Parameter(description = "The appointment's id")
            int appointmentId) {
        try {
            appointmentsService.cancelAppointment(appointmentId);
            return new EmptyAppResponse(200, null);
        }
        catch (IllegalArgumentException e) {
            return new EmptyAppResponse(400, e.getMessage());
        }
        catch (NoSuchElementException e) {
            return new EmptyAppResponse(404, e.getMessage());
        }
        catch (Exception e) {
            return new EmptyAppResponse(500, e.getMessage());
        }
    }

    @Operation(
            summary = "All the appointments of a given client.",
            description = "Returns all the appointments (past and future) of a client, given its id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All the appointments of the given client.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "The client is not registered."),
                    @ApiResponse(responseCode = "500", description = "Some unknown error happened.")
            }
    )
    public AppResponse<Collection<AppointmentDto>> getAppointmentsFromClient(
            @Parameter(description = "The client's id.")
            int clientId
    ) {
        try {
            return new AppResponse<>(200, appointmentsService.getAppointmentsOfClient(clientId), null);
        }
        catch (NoSuchElementException e) {
            return new AppResponse<>(404, null, e.getMessage());
        }
        catch (Exception e) {
            return new AppResponse<>(500, null, e.getMessage());
        }
    }
}
