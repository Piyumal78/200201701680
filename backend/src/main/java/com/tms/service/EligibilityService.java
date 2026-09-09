package com.tms.service;

import com.tms.dto.EligibilityCheckRequest;
import com.tms.dto.EligibilityResponse;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EligibilityService {

    private final OfficerRepository officerRepository;
    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final NominationRepository nominationRepository;

    public EligibilityService(OfficerRepository officerRepository,
                              TrainingProgrammeRepository trainingProgrammeRepository,
                              NominationRepository nominationRepository) {
        this.officerRepository = officerRepository;
        this.trainingProgrammeRepository = trainingProgrammeRepository;
        this.nominationRepository = nominationRepository;
    }

    public EligibilityResponse checkEligibility(EligibilityCheckRequest request) {
        if (request == null || request.getTrainingId() == null || request.getOfficerId() == null) {
            return new EligibilityResponse(false, "Invalid request.", "Training ID and Officer ID are required.");
        }

        // 1. Fetch Officer & Training
        Officer officer = officerRepository.findById(request.getOfficerId()).orElse(null);
        if (officer == null) {
            return new EligibilityResponse(false, "Officer not found.", "No officer found with ID: " + request.getOfficerId());
        }

        TrainingProgramme training = trainingProgrammeRepository.findById(request.getTrainingId()).orElse(null);
        if (training == null) {
            return new EligibilityResponse(false, "Training programme not found.", "No training programme found with ID: " + request.getTrainingId());
        }

        // 2. Step 1: Check Department
        List<String> allowedDepts = training.getAllowedDepartments();
        if (allowedDepts != null && !allowedDepts.isEmpty()) {
            boolean deptMatches = allowedDepts.stream()
                    .anyMatch(d -> d.equalsIgnoreCase(officer.getDepartment()) || 
                                   officer.getDepartment().toLowerCase().contains(d.toLowerCase()));
            if (!deptMatches) {
                String allowedList = String.join(", ", allowedDepts);
                return new EligibilityResponse(
                        false,
                        "Officer is not eligible for this training programme.",
                        "Officer belongs to " + officer.getDepartment() + " department. Required department: " + allowedList + "."
                );
            }
        }

        // 3. Step 2: Check Grade
        String requiredGrade = training.getRequiredGrade();
        if (requiredGrade != null && !requiredGrade.trim().isEmpty()) {
            List<String> grades = Arrays.asList(requiredGrade.split(",\\s*"));
            boolean gradeMatches = grades.stream()
                    .anyMatch(g -> g.equalsIgnoreCase(officer.getGrade()) || 
                                   (officer.getGrade() != null && officer.getGrade().toLowerCase().contains(g.toLowerCase())));
            if (!gradeMatches) {
                return new EligibilityResponse(
                        false,
                        "Officer is not eligible for this training programme.",
                        "Officer has " + officer.getGrade() + ". Required grade: " + requiredGrade + "."
                );
            }
        }

        // 4. Step 3: Check Years of Service
        Integer minServiceYears = training.getMinimumYearsOfService();
        if (minServiceYears != null && minServiceYears > 0) {
            int officerService = officer.getYearsOfService() != null ? officer.getYearsOfService() : 0;
            if (officerService < minServiceYears) {
                return new EligibilityResponse(
                        false,
                        "Officer is not eligible for this training programme.",
                        "Officer has " + officerService + " years of service. Minimum required: " + minServiceYears + " years."
                );
            }
        }

        // 5. Step 4: Check Previous 12 Months Participation
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        boolean participatedRecently = nominationRepository.existsByOfficerAndProgrammeAndParticipationDateAfter(
                officer,
                training,
                twelveMonthsAgo
        );

        if (participatedRecently) {
            return new EligibilityResponse(
                    false,
                    "Officer is not eligible.",
                    "Officer participated in this training within the previous 12 months."
            );
        }

        // 6. All Checks Passed -> Eligible
        return new EligibilityResponse(
                true,
                "Officer is eligible for this training programme."
        );
    }
}
