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
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TrainingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingManagementApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  Task 3 Training Eligibility Backend Running    ");
        System.out.println("  Endpoint: POST http://localhost:8080/api/eligibility/check ");
        System.out.println("=================================================");
    }

    @Bean
    public CommandLineRunner initData(TrainingProgrammeRepository progRepo,
                                      OfficerRepository offRepo,
                                      NominationRepository nomRepo) {
        return args -> {
            // 1. Seed / Update Training Programmes
            TrainingProgramme p1 = new TrainingProgramme(
                    1L,
                    "Financial Management Programme",
                    Arrays.asList("Finance", "Budget", "Planning"),
                    null,
                    null
            );

            TrainingProgramme p2 = new TrainingProgramme(
                    2L,
                    "Technical Programme",
                    Arrays.asList("IT", "ICT"),
                    null,
                    null
            );

            TrainingProgramme p3 = new TrainingProgramme(
                    3L,
                    "Management Development Programme",
                    List.of(), // Open to all departments
                    "Grade I, Grade II",
                    5
            );

            progRepo.saveAll(Arrays.asList(p1, p2, p3));

            // 2. Seed / Update Officers
            Officer o1 = new Officer(1L, "A. Perera", "Finance", "Grade III", 6);
            Officer o2 = new Officer(2L, "B. Silva", "IT", "Grade II", 6);
            Officer o3 = new Officer(3L, "C. Fernando", "Planning", "Grade I", 10);
            Officer o4 = new Officer(4L, "D. Bandara", "Administration", "Grade III", 2);
            Officer o5 = new Officer(5L, "E. Jayawardena", "HR", "Grade I", 8);
            Officer o6 = new Officer(6L, "F. Gunasekara", "Budget", "Grade II", 5);
            Officer o7 = new Officer(7L, "G. Wickramasinghe", "ICT", "Grade III", 4);

            offRepo.saveAll(Arrays.asList(o1, o2, o3, o4, o5, o6, o7));

            // 3. Seed / Update Previous Participation Records
            if (nomRepo.count() == 0) {
                // B. Silva (ID 2) attended Technical Programme (ID 2) 6 months ago (Within 12 months)
                Nomination n1 = new Nomination(p2, o2, LocalDateTime.now().minusMonths(6), "ATTENDED");

                // C. Fernando (ID 3) attended Financial Management (ID 1) 18 months ago (> 12 months)
                Nomination n2 = new Nomination(p1, o3, LocalDateTime.now().minusMonths(18), "ATTENDED");

                // E. Jayawardena (ID 5) attended Management Development (ID 3) 8 months ago (Within 12 months)
                Nomination n3 = new Nomination(p3, o5, LocalDateTime.now().minusMonths(8), "ATTENDED");

                nomRepo.saveAll(Arrays.asList(n1, n2, n3));
            }

            System.out.println("✅ Database Updated: Training Programmes & Officers synced successfully.");
        };
    }
}
