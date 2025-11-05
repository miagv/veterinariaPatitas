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
            // Crear servicios (si no existen ya) -> evita duplicados entre data.sql y DataInitializer
            ServiceVet consultaGeneral = serviceRepo.findAll().stream()
                    .filter(s -> "Consulta General".equals(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceVet s = new ServiceVet();
                        s.setName("Consulta General");
                        s.setDescription("Revisión general de la mascota");
                        s.setPrice(50.0);
                        s.setDuration("30min");
                        return serviceRepo.save(s);
                    });

            ServiceVet vacunacion = serviceRepo.findAll().stream()
                    .filter(s -> "Vacunación".equals(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceVet s = new ServiceVet();
                        s.setName("Vacunación");
                        s.setDescription("Servicio de vacunación y refuerzos");
                        s.setPrice(30.0);
                        s.setDuration("15min");
                        return serviceRepo.save(s);
                    });

            ServiceVet cirugia = serviceRepo.findAll().stream()
                    .filter(s -> "Cirugía".equals(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceVet s = new ServiceVet();
                        s.setName("Cirugía");
                        s.setDescription("Procedimientos quirúrgicos");
                        s.setPrice(250.0);
                        s.setDuration("120min");
                        return serviceRepo.save(s);
                    });

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

            // Crear servicios adicionales que puedan venir desde data.sql
            ServiceVet grooming = serviceRepo.findAll().stream()
                    .filter(s -> "Grooming".equals(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceVet s = new ServiceVet();
                        s.setName("Grooming");
                        s.setDescription("Servicio de peluquería y baño");
                        s.setPrice(40.0);
                        s.setDuration("45min");
                        return serviceRepo.save(s);
                    });

            ServiceVet emergencia = serviceRepo.findAll().stream()
                    .filter(s -> "Emergencia".equals(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceVet s = new ServiceVet();
                        s.setName("Emergencia");
                        s.setDescription("Atención de emergencias 24/7");
                        s.setPrice(0.0);
                        s.setDuration("varios");
                        return serviceRepo.save(s);
                    });

            // Añadir médicos extra que aparecen en la sección "Sobre Nosotros"
            // Solo los creamos si no existen por nombre
            if (medicoRepo.findAll().stream().noneMatch(m -> "Aimed Gonzales".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Aimed Gonzales");
                m.setEspecialidad("Cirugía y Diagnóstico por Imagen");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(cirugia);
                medicoRepo.save(m);
            }

            if (medicoRepo.findAll().stream().noneMatch(m -> "Angel Shinno".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Angel Shinno");
                m.setEspecialidad("Vacunación, Desparasitación y Nutrición");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(vacunacion);
                medicoRepo.save(m);
            }

            if (medicoRepo.findAll().stream().noneMatch(m -> "Norvan Sifuentes".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Norvan Sifuentes");
                m.setEspecialidad("Dermatología");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(consultaGeneral);
                medicoRepo.save(m);
            }

            if (medicoRepo.findAll().stream().noneMatch(m -> "Angel Aguirre".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Angel Aguirre");
                m.setEspecialidad("Traumatología");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(consultaGeneral);
                medicoRepo.save(m);
            }

            if (medicoRepo.findAll().stream().noneMatch(m -> "Mario Flores".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Mario Flores");
                m.setEspecialidad("Dermatología");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(consultaGeneral);
                medicoRepo.save(m);
            }

            if (medicoRepo.findAll().stream().noneMatch(m -> "Rolando Campos".equals(m.getNombre()))) {
                Medico m = new Medico();
                m.setNombre("Rolando Campos");
                m.setEspecialidad("Laboratorio Clínico y Peluquería Canina y Felina");
                m.setHorarioInicio(LocalTime.of(9, 0));
                m.setHorarioFin(LocalTime.of(17, 0));
                m.setServicio(grooming);
                medicoRepo.save(m);
            }

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
