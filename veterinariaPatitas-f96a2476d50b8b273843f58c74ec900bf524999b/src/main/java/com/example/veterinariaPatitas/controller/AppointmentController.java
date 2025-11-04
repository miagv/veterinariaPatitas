/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.service.AppointmentService;
import com.example.veterinariaPatitas.model.Medico;
import com.example.veterinariaPatitas.model.ServiceVet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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
        List<ServiceVet> services = appointmentService.getAvailableServices();
        List<Medico> medicos = appointmentService.getAllMedicos();

        System.out.println("Número de servicios encontrados: " + services.size());
        System.out.println("Número de médicos encontrados: " + medicos.size());
        
        for (Medico medico : medicos) {
            System.out.println("Médico: " + medico.getNombre() + 
                             ", Servicio: " + (medico.getServicio() != null ? medico.getServicio().getName() : "null") +
                             ", Horario: " + medico.getHorarioInicio() + " - " + medico.getHorarioFin());
        }

        model.addAttribute("availableServices", services);
        model.addAttribute("bookedAppointments", appointmentService.getBookedAppointments());
        model.addAttribute("allMedicos", medicos);
        
        LocalDate minDate = LocalDate.now().plusDays(1);
        model.addAttribute("minDate", minDate.format(DateTimeFormatter.ISO_DATE));

        return "simulador_citas";
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
}
