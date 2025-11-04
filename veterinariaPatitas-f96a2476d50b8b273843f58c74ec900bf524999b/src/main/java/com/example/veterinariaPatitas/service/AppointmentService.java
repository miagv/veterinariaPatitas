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

        // Si el médico no tiene horario definido en la entidad, evitamos NPE y
        // permitimos (por ahora) la validación de horario — registramos advertencia.
        if (inicio == null || fin == null) {
            System.out.println("⚠️ Aviso: El médico (id=" + medicoId + ") no tiene horario definido en la entidad. Se omite la validación de horario.");
        } else {
            if (horaCita.isBefore(inicio) || horaCita.isAfter(fin)) {
                System.out.println("⛔ La hora seleccionada está fuera del horario del médico.");
                return false;
            }
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
}
