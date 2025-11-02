package com.example.veterinariaPatitas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private ServiceVet servicio;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private String cliente;
    private LocalDateTime fechaHora;

    public Cita() {}

    public Cita(ServiceVet servicio, Medico medico, String cliente, LocalDateTime fechaHora) {
        this.servicio = servicio;
        this.medico = medico;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
    }

    public Long getId() {
        return id;
    }

    public ServiceVet getServicio() {
        return servicio;
    }

    public void setServicio(ServiceVet servicio) {
        this.servicio = servicio;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}

