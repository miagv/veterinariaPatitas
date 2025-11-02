/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.veterinariaPatitas.repository;

import com.example.veterinariaPatitas.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ¡Añadir esta importación!

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // AÑADIR esta anotación para forzar una consulta directa y precisa:
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.dateTime = ?1") 
    boolean existsByDateTime(LocalDateTime dateTime);
}