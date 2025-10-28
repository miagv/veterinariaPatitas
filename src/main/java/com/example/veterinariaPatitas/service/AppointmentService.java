package com.example.veterinariaPatitas.service;

import com.example.veterinariaPatitas.model.Appointment;
import com.example.veterinariaPatitas.model.ServiceVet;
import com.example.veterinariaPatitas.repository.ServiceRepository;
import com.example.veterinariaPatitas.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // 🌟 MÉTODO FALTANTE 1: Obtener la lista de servicios disponibles
    public List<ServiceVet> getAvailableServices() {
        return serviceRepository.findAll();
    }

    // 🌟 MÉTODO FALTANTE 2: Obtener la lista de citas agendadas
    public List<Appointment> getBookedAppointments() {
        return appointmentRepository.findAll();
    }

    // Verificar disponibilidad (TRUE si NO existe la cita)
    public boolean isAvailable(LocalDateTime dateTime) {
        return !appointmentRepository.existsByDateTime(dateTime);
    }

    // Agendar cita
   public boolean bookAppointment(int serviceId, LocalDateTime dateTime, String clientName) {
    // 1. Obtener Servicio
    ServiceVet service = serviceRepository.findById(serviceId).orElse(null); 
    
    if (service == null) return false;

    
    
    // Si queremos estar seguros de NO guardar duplicados:
    if (!isAvailable(dateTime)) {
        return false; // Si ya está ocupada, no la guardamos.
    }

    // 3. Guardar
    Appointment appointment = new Appointment(service, dateTime, clientName);
    appointmentRepository.save(appointment);
    return true;
}

    // Obtener servicio por ID
    public ServiceVet getServiceById(int serviceId) {
        // Corrección de ID: usar int directamente
        return serviceRepository.findById(serviceId).orElse(null);
    }
}