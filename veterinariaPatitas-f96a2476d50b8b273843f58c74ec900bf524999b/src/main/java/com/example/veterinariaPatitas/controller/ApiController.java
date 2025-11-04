package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Medico;
import com.example.veterinariaPatitas.model.ServiceVet;
import com.example.veterinariaPatitas.repository.MedicoRepository;
import com.example.veterinariaPatitas.repository.ServiceVetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api") // todas las rutas empezarán con /api
public class ApiController {

    @Autowired
    private ServiceVetRepository serviceVetRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // ✅ Endpoint para listar todos los servicios (para el combobox de servicios)
    @GetMapping("/servicios")
    public List<ServiceVet> listarServicios() {
        return serviceVetRepository.findAll();
    }

    // ✅ Endpoint para listar médicos disponibles según servicio y hora
    @GetMapping("/medicos/disponibles")
public List<Medico> listarMedicosDisponibles(
        @RequestParam("servicioId") Integer servicioId,  // 👈 cambia de Long a Integer
        @RequestParam("hora") String horaStr) {

    LocalTime hora = LocalTime.parse(horaStr);

    return medicoRepository.findAll().stream()
            .filter(m -> m.getServicio() != null 
                    && m.getServicio().getId() == servicioId) // 👈 usa == para comparar int
            .filter(m -> !hora.isBefore(m.getHorarioInicio()) && !hora.isAfter(m.getHorarioFin()))
            .collect(Collectors.toList());
    }
}

