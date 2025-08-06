package org.example.entities;

import java.time.LocalDateTime;

public class Appointment {
    private Integer id;
    private Integer clientId;
    private LocalDateTime dateTime;

    public Appointment() { }

    public Appointment(Integer id, Integer clientId, LocalDateTime dateTime) {
        this.id = id;
        this.clientId = clientId;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
