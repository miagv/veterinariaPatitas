package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Appointment;
import com.example.veterinariaPatitas.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // 👈 ¡Clave de seguridad!
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/management/appointments") // Ruta protegida
public class AppointmentManagementController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentManagementController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    /**
     * Obtiene todas las citas. Solo TRABAJADOR.
     */
    @PreAuthorize("hasAuthority('TRABAJADOR')")
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getBookedAppointments();
    }
    
    /**
     * Actualiza una cita existente (fecha, hora, servicio o médico). Solo TRABAJADOR.
     */
    @PreAuthorize("hasAuthority('TRABAJADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Appointment updatedAppointment) {
        
        // Verifica si la cita existe
        if (appointmentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Llama al servicio para actualizar
            Appointment savedAppointment = appointmentService.updateAppointment(id, updatedAppointment);
            return ResponseEntity.ok(savedAppointment);
        } catch (IllegalArgumentException e) {
            // Captura errores de lógica (ej. hora no disponible, ID no encontrado)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Elimina una cita por ID. Solo TRABAJADOR.
     */
    @PreAuthorize("hasAuthority('TRABAJADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        
        if (appointmentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        appointmentService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
