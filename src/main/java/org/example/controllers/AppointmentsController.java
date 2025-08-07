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

@Tag(name = "Turnos", description = "Operaciones relacionadas con reservas de turnos")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }


    @Operation(
            tags = { "Turnos" },
            summary = " Reserva un Turno",
            description = "Reserva un turno para un usuario registrado dentro de los días y horas laborales (de Lunes a Viernes entre las 9:00 y las 18:00). Retorna la reserva creada.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Los datos de la reserva creada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = " Uno de los datos pasados no era correcto."),
                    @ApiResponse(responseCode = "404", description = "El cliente no está registrado."),
                    @ApiResponse(responseCode = "500", description = "Ocurrió un error desconocido.")
            }
    )
    public AppResponse<AppointmentDto> makeAppointment(
            @Parameter(description = "Los datos mínimos para reservar un turno.")
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
            tags = { "Turnos" },
            summary = "Cancela una reserva.",
            description = "Cancela una reserva en una fecha futura a partir de su id.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "La reserva fue cancelada exitosamente.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyAppResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Intentó cancelar una reserva pasada."),
                    @ApiResponse(responseCode = "404", description = "La reserva no se pudo encontrar."),
                    @ApiResponse(responseCode = "500", description = "Ocurrió un error desconocido.")
            }
    )
    public EmptyAppResponse cancelAppointment(
            @Parameter(description = "El id de la reserva.")
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
            tags = { "Turnos" },
            summary = "Todas las reservas de un cliente.",
            description = "Retorna todas las reservas de un cliente (futuras y pasadas) a partir del id del cliente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Todas las reservas del cliente.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "El cliente no está registrado."),
                    @ApiResponse(responseCode = "500", description = "Ocurrió un error desconocido.")
            }
    )
    public AppResponse<Collection<AppointmentDto>> getAppointmentsFromClient(
            @Parameter(description = "El id del cliente.")
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
