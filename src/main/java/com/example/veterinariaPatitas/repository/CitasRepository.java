package com.example.veterinariaPatitas.repository;

import com.example.veterinariaPatitas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitasRepository extends JpaRepository<Cita, Long> {
}

