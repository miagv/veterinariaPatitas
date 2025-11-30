package com.example.veterinariaPatitas.service;

import com.example.veterinariaPatitas.model.Appointment;
import com.example.veterinariaPatitas.model.Medico;
import com.example.veterinariaPatitas.model.ServiceVet;
import com.example.veterinariaPatitas.repository.ServiceRepository;
import com.example.veterinariaPatitas.repository.AppointmentRepository;
import com.example.veterinariaPatitas.repository.MedicoRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional; 

@Service
public class AppointmentService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // 🌟 1. Obtener lista de servicios disponibles
    public List<ServiceVet> getAvailableServices() {
        return serviceRepository.findAll();
    }

    // 🌟 2. Obtener lista de citas agendadas
    public List<Appointment> getBookedAppointments() {
        return appointmentRepository.findAll();
    }

    // 🌟 3. Verificar disponibilidad de hora
    public boolean isAvailable(LocalDateTime dateTime) {
        return !appointmentRepository.existsByDateTime(dateTime);
    }

    // 🌟 4. Agendar cita (con servicio y médico)
    public boolean bookAppointment(int serviceId, Long medicoId, LocalDateTime appointmentDateTime, String clientName) {
        // 1️⃣ Buscar servicio
        ServiceVet service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) return false;

        // 2️⃣ Buscar médico
        Medico medico = medicoRepository.findById(medicoId).orElse(null);
        if (medico == null) return false;

        // 3️⃣ Validar si la hora de la cita está dentro del horario del médico
        LocalTime inicio = medico.getHorarioInicio();
        LocalTime fin = medico.getHorarioFin();
        LocalTime horaCita = appointmentDateTime.toLocalTime();

        if (horaCita.isBefore(inicio) || horaCita.isAfter(fin)) {
            System.out.println("⛔ La hora seleccionada está fuera del horario del médico.");
            return false;
        }

        // 4️⃣ Verificar disponibilidad
        if (!isAvailable(appointmentDateTime)) {
            System.out.println("⛔ La hora ya está ocupada.");
            return false;
        }

        // 5️⃣ Crear nueva cita
        Appointment appointment = new Appointment(service, appointmentDateTime, clientName);
        appointment.setMedico(medico);
        appointmentRepository.save(appointment);

        System.out.println("✅ Cita registrada correctamente para " + clientName);
        return true;
    }

    // 🌟 5. Obtener servicio por ID
    public ServiceVet getServiceById(int serviceId) {
        return serviceRepository.findById(serviceId).orElse(null);
    }

    // 🌟 6. Obtener todos los médicos
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    // 🌟 7. Obtener cita por ID (Usado por los Controllers)
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    // 🌟 8. Eliminar cita por ID
    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

// Archivo: AppointmentService.java

// ... (dentro de la clase AppointmentService) ...

// ===============================================
// === 🌟 9. MÉTODO DE ACTUALIZACIÓN (VERSIÓN FINAL CORREGIDA) ===
// ===============================================

public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
    
    // 1. Obtener la cita existente
    Appointment existingAppointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));

    // 2. Verificar la disponibilidad de la NUEVA fecha/hora
    LocalDateTime newDateTime = updatedAppointment.getDateTime();
    
    if (!existingAppointment.getDateTime().isEqual(newDateTime) && appointmentRepository.existsByDateTime(newDateTime)) {
        throw new IllegalArgumentException("Error: La nueva hora seleccionada ya está ocupada por otra cita.");
    }

    // 3. Aplicar los cambios
    existingAppointment.setDateTime(newDateTime);
    
    // CORRECCIÓN para evitar NullPointerException y la comparación int/null
    if (updatedAppointment.getService() != null && updatedAppointment.getService().getId() != null) {
        // Almacenamos el ID de ServiceVet (que puede ser Integer)
        Integer serviceId = updatedAppointment.getService().getId(); 

        ServiceVet newService = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado."));
        existingAppointment.setService(newService);
    }
    
    // CORRECCIÓN para evitar NullPointerException
    if (updatedAppointment.getMedico() != null && updatedAppointment.getMedico().getId() != null) {
        Long medicoId = updatedAppointment.getMedico().getId(); 
        
        Medico newMedico = medicoRepository.findById(medicoId)
            .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado."));
        existingAppointment.setMedico(newMedico);
    }
    
    existingAppointment.setClientName(updatedAppointment.getClientName());

    // 4. Guardar y devolver
    return appointmentRepository.save(existingAppointment);
}
}