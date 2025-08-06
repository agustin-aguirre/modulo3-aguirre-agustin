package org.example.controllers;

import org.example.services.appointments.AppointmentsService;

public class AppointmentsController {
    private final AppointmentsService service;

    public AppointmentsController(AppointmentsService service) {
        this.service = service;
    }
}
