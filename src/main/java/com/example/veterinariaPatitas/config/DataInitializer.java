package com.example.veterinariaPatitas.config;

import com.example.veterinariaPatitas.model.Medico;
import com.example.veterinariaPatitas.model.Product;
import com.example.veterinariaPatitas.model.ServiceVet;
import com.example.veterinariaPatitas.repository.MedicoRepository;
import com.example.veterinariaPatitas.repository.ProductRepository;
import com.example.veterinariaPatitas.repository.ServiceVetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalTime;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(
        ServiceVetRepository serviceRepo,
        MedicoRepository medicoRepo,
        ProductRepository productRepo) {
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

            // Seed products if table empty
            long existing = productRepo.count();
            log.info("Existing productos count: {}", existing);
            if (existing == 0) {
                Product p1 = new Product();
                p1.setName("Purina");
                p1.setPrice(1200.0);
                p1.setStock(10);
                p1.setUnit("unidad");
                productRepo.save(p1);

                Product p2 = new Product();
                p2.setName("Osito");
                p2.setPrice(850.0);
                p2.setStock(5);
                p2.setUnit("unidad");
                productRepo.save(p2);

                Product p3 = new Product();
                p3.setName("Pepon");
                p3.setPrice(500.0);
                p3.setStock(20);
                p3.setUnit("unidad");
                productRepo.save(p3);

                log.info("Seeded default products into productos table (3 items)");
            } else {
                log.info("Skipped seeding products because table is not empty");
            }
        };
    }
}
