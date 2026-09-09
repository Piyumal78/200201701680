package com.tms;

import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class TrainingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingManagementApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  TMS Spring Boot Backend Running on Port 8080   ");
        System.out.println("  H2 Database Console: http://localhost:8080/h2-console ");
        System.out.println("=================================================");
    }

    @Bean
    public CommandLineRunner initData(TrainingProgrammeRepository progRepo,
                                     OfficerRepository offRepo,
                                     NominationRepository nomRepo) {
        return args -> {
            TrainingProgramme prog = progRepo.findById(1L).orElse(null);
            if (prog != null && nomRepo.count() == 0) {
                Officer off1 = offRepo.findById(101L).orElse(null);
                Officer off2 = offRepo.findById(102L).orElse(null);
                Officer off3 = offRepo.findById(103L).orElse(null);
                Officer off4 = offRepo.findById(104L).orElse(null);

                if (off1 != null) {
                    Nomination n1 = new Nomination();
                    n1.setProgramme(prog);
                    n1.setOfficer(off1);
                    n1.setNominatedAt(LocalDateTime.now().minusMinutes(30));
                    n1.setStatus("CONFIRMED");
                    nomRepo.save(n1);
                }
                if (off2 != null) {
                    Nomination n2 = new Nomination();
                    n2.setProgramme(prog);
                    n2.setOfficer(off2);
                    n2.setNominatedAt(LocalDateTime.now().minusMinutes(25));
                    n2.setStatus("CONFIRMED");
                    nomRepo.save(n2);
                }
                if (off3 != null) {
                    Nomination n3 = new Nomination();
                    n3.setProgramme(prog);
                    n3.setOfficer(off3);
                    n3.setNominatedAt(LocalDateTime.now().minusMinutes(20));
                    n3.setStatus("CONFIRMED");
                    nomRepo.save(n3);
                }
                if (off4 != null) {
                    Nomination n4 = new Nomination();
                    n4.setProgramme(prog);
                    n4.setOfficer(off4);
                    n4.setNominatedAt(LocalDateTime.now().minusMinutes(15));
                    n4.setStatus("WAITING");
                    nomRepo.save(n4);
                }
            }
        };
    }
}
