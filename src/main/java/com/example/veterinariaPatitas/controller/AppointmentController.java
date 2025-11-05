/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Appointment; // 👈 NECESARIO para manejar el objeto cita
import com.example.veterinariaPatitas.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping; // 👈 NECESARIO para el método DELETE
import org.springframework.web.bind.annotation.PathVariable; // 👈 NECESARIO para obtener el ID de la URL
import org.springframework.web.bind.annotation.ResponseBody; // 👈 NECESARIO para devolver JSON/texto
import org.springframework.http.ResponseEntity; // 👈 NECESARIO para devolver respuesta HTTP
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Muestra la vista del simulador de agendamiento.
     */
    @GetMapping("/simulador_citas")
    public String showAppointmentSimulator(Model model) {

    model.addAttribute("availableServices", appointmentService.getAvailableServices());

    model.addAttribute("bookedAppointments", appointmentService.getBookedAppointments());

    // Enviamos la lista completa de médicos (para poblar el combobox y permitir filtrado cliente-side)
    model.addAttribute("allMedicos", appointmentService.getAllMedicos());

    LocalDate minDate = LocalDate.now().plusDays(1);

    model.addAttribute("minDate", minDate.format(DateTimeFormatter.ISO_DATE));

        return "simulador_citas"; // Retorna la vista
    }

   
  
  // AppointmentController.java - Método bookAppointment corregido
@PostMapping("/book_appointment")
public String bookAppointment(
        @RequestParam("serviceId") int serviceId,
        @RequestParam("medicoId") Long medicoId, // 👈 nuevo parámetro
        @RequestParam("date") LocalDate date,
        @RequestParam("time") LocalTime time,
        @RequestParam("clientName") String clientName,
        Model model) {


    LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

    // 1. Verificar disponibilidad de forma clara (Usamos el resultado de isAvailable directamente)
    boolean isAvailable = appointmentService.isAvailable(appointmentDateTime);

    if (!isAvailable) { // 👈 CORRECCIÓN: Entra aquí SI NO está disponible (hora OCUPADA)
        
        // Muestra mensaje de error (Ocupado)
        model.addAttribute("message", "Error: ¡Lo sentimos! La hora seleccionada (" + appointmentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + ") ya está ocupada. Por favor, elige otro horario.");
        model.addAttribute("messageType", "danger");
        
    } else {
        
        // 2. Procede a agendar la cita (Ya sabemos que está disponible)
        boolean success = appointmentService.bookAppointment(serviceId, medicoId, appointmentDateTime, clientName);


        if (success) {
            model.addAttribute("message", "¡Éxito! Su cita ha sido agendada con el servicio de " + appointmentService.getServiceById(serviceId).getName() + " para el " + appointmentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + ".");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Error: No se pudo agendar la cita. El servicio podría no existir.");
            model.addAttribute("messageType", "danger");
        }
    }
    
    // Recarga la vista con los datos actualizados
    return showAppointmentSimulator(model);
}

// Nuevo método para eliminar citas
@DeleteMapping("/citas/eliminar/{id}") // 1. Mapea la URL DELETE /citas/eliminar/ID
@ResponseBody
public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
    try {
        // 2. Verifica si la cita existe antes de intentar borrarla (Buena práctica de seguridad)
        Optional<Appointment> appointment = appointmentService.findById(id); 

        if (appointment.isPresent()) {
            // 3. Llama al servicio para eliminar
            appointmentService.delete(id); 
            // 4. Devuelve un código HTTP 200 (OK) al JavaScript
            return ResponseEntity.ok("Cita eliminada exitosamente."); 
        } else {
            // 5. Devuelve un código HTTP 404 (No encontrada)
            return ResponseEntity.notFound().build(); 
        }
    } catch (Exception e) {
        // 6. Devuelve un código HTTP 500 (Error interno del servidor)
        return ResponseEntity.status(500).body("Error al eliminar la cita: " + e.getMessage());
    }
}
}

