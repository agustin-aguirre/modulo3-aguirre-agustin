package org.example.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.dtos.appointments.AppointmentDto;
import org.example.dtos.appointments.MakeAppointmentDto;
import org.example.services.appointments.AppointmentsService;
import org.example.services.clients.ClientsService;

@Path("/turnos")
@Tag(name = "turnos", description = "Operaciones simuladas de gestión de turnos")
public class SimpleApplicationController {

    private final ClientsService clientsService;
    private final AppointmentsService appointmentsService;

    public SimpleApplicationController(ClientsService clientsService, AppointmentsService appointmentsService) {
        this.clientsService = clientsService;
        this.appointmentsService = appointmentsService;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Registra un nuevo turno",
            description = "Crea un turno para un cliente exitente",
            tags = { "turnos" }
    )
    @RequestBody(
            description = "Datos del turno a registrar",
            required = true,
            content = @Content(schema = @Schema(implementation = MakeAppointmentDto.class))
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Turno registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public void makeAppointment(MakeAppointmentDto makeAppointmentDto) {

    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Elimina un turno por ID",
            description = "Elimina un turno si existe",
            tags = { "turnos" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Turno eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Turno no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public void cancelAppointment(
            @Parameter(description = "ID del turno", required = true, example = "1")
            @PathParam("id")
            int appointmentId) {

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtener turnos de un cliente según su ID",
            description = "Retorna los turnos del cliente si existe.",
            tags = { "Clientes" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public void getAppointmentsFromClient(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathParam("id") int clientId
    ) {

    }
}
