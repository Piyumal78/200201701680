package com.tms.controller;

import com.tms.dto.EligibilityCheckRequest;
import com.tms.dto.EligibilityResponse;
import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import com.tms.service.EligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eligibility")
@CrossOrigin(origins = "*")
public class EligibilityController {

    private final EligibilityService eligibilityService;
    private final OfficerRepository officerRepository;
    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final NominationRepository nominationRepository;

    public EligibilityController(EligibilityService eligibilityService,
                                 OfficerRepository officerRepository,
                                 TrainingProgrammeRepository trainingProgrammeRepository,
                                 NominationRepository nominationRepository) {
        this.eligibilityService = eligibilityService;
        this.officerRepository = officerRepository;
        this.trainingProgrammeRepository = trainingProgrammeRepository;
        this.nominationRepository = nominationRepository;
    }

    /**
     * Main Task 3 Endpoint: Check Eligibility
     * POST /api/eligibility/check
     */
    @PostMapping("/check")
    public ResponseEntity<EligibilityResponse> checkEligibility(@RequestBody EligibilityCheckRequest request) {
        EligibilityResponse response = eligibilityService.checkEligibility(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Helper endpoint: Get all officers
     * GET /api/eligibility/officers
     */
    @GetMapping("/officers")
    public ResponseEntity<List<Officer>> getAllOfficers() {
        return ResponseEntity.ok(officerRepository.findAll());
    }

    /**
     * Helper endpoint: Get all training programmes
     * GET /api/eligibility/trainings
     */
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingProgramme>> getAllTrainings() {
        return ResponseEntity.ok(trainingProgrammeRepository.findAll());
    }

    /**
     * Helper endpoint: Get officer participation history
     * GET /api/eligibility/officers/{id}/history
     */
    @GetMapping("/officers/{id}/history")
    public ResponseEntity<List<Nomination>> getOfficerHistory(@PathVariable Long id) {
        Officer officer = officerRepository.findById(id).orElse(null);
        if (officer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nominationRepository.findByOfficerOrderByParticipationDateDesc(officer));
    }
}
