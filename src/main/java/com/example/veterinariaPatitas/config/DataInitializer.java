package com.example.veterinariaPatitas.config;

import com.example.veterinariaPatitas.model.Medico;
import com.example.veterinariaPatitas.model.ServiceVet;
import com.example.veterinariaPatitas.repository.MedicoRepository;
import com.example.veterinariaPatitas.repository.ServiceVetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            ServiceVetRepository serviceRepo,
            MedicoRepository medicoRepo) {
        return args -> {
            // Crear servicios
            ServiceVet consultaGeneral = new ServiceVet();
            consultaGeneral.setName("Consulta General");
            consultaGeneral.setDescription("Revisión general de la mascota");
            serviceRepo.save(consultaGeneral);

            ServiceVet vacunacion = new ServiceVet();
            vacunacion.setName("Vacunación");
            vacunacion.setDescription("Servicio de vacunación y refuerzos");
            serviceRepo.save(vacunacion);

            ServiceVet cirugia = new ServiceVet();
            cirugia.setName("Cirugía");
            cirugia.setDescription("Procedimientos quirúrgicos");
            serviceRepo.save(cirugia);

            // Crear médicos
            Medico medico1 = new Medico();
            medico1.setNombre("Juan");
            medico1.setEspecialidad("Medicina General");
            medico1.setHorarioInicio(LocalTime.of(9, 0));
            medico1.setHorarioFin(LocalTime.of(17, 0));
            medico1.setServicio(consultaGeneral);
            medicoRepo.save(medico1);

            Medico medico2 = new Medico();
            medico2.setNombre("María");
            medico2.setEspecialidad("Cirujana");
            medico2.setHorarioInicio(LocalTime.of(10, 0));
            medico2.setHorarioFin(LocalTime.of(18, 0));
            medico2.setServicio(cirugia);
            medicoRepo.save(medico2);

            Medico medico3 = new Medico();
            medico3.setNombre("Carlos");
            medico3.setEspecialidad("Medicina General");
            medico3.setHorarioInicio(LocalTime.of(8, 0));
            medico3.setHorarioFin(LocalTime.of(16, 0));
            medico3.setServicio(consultaGeneral);
            medicoRepo.save(medico3);
        };
    }
}
