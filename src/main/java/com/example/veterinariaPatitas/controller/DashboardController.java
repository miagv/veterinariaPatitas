package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.service.AppointmentService;
import com.example.veterinariaPatitas.service.SalesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class DashboardController {

    private final SalesService salesService;
    private final AppointmentService appointmentService;

    @Autowired
    public DashboardController(SalesService salesService, AppointmentService appointmentService) {
        this.salesService = salesService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // --- Total de ventas hoy ---
            Double totalVentasHoy = salesService.getTotalVentasHoy();
            if (totalVentasHoy == null) totalVentasHoy = 0.0;

            // --- Ventas por mes ---
            Map<String, Double> ventasMap = salesService.getVentasPorMes();
            if (ventasMap == null) ventasMap = Collections.emptyMap();

            // --- Citas pendientes ---
            Long citasPendientes = appointmentService.contarCitasPendientes();
            if (citasPendientes == null) citasPendientes = 0L;

            // --- Citas por mes ---
            Map<String, Long> citasMap = appointmentService.getCitasPorMes();
            if (citasMap == null) citasMap = Collections.emptyMap();

            // --- Convertir a JSON seguro ---
            String ventasJson = mapper.writeValueAsString(ventasMap);
            String citasJson = mapper.writeValueAsString(citasMap);

            // --- Agregar al modelo ---
            model.addAttribute("totalVentasHoy", totalVentasHoy);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("ventasJson", ventasJson);
            model.addAttribute("citasJson", citasJson);

        } catch (Exception e) {
            e.printStackTrace();
            // Valores por defecto para evitar errores 500
            model.addAttribute("totalVentasHoy", 0.0);
            model.addAttribute("citasPendientes", 0L);
            model.addAttribute("ventasJson", "{}");
            model.addAttribute("citasJson", "{}");
        }

        return "dashboard";
    }
}
