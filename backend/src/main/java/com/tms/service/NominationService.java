package com.tms.service;

import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NominationService {

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private TrainingProgrammeRepository programmeRepository;

    private static final int DEFAULT_DEPT_QUOTA = 15;

    public List<Nomination> getAllNominations() {
        return nominationRepository.findAll();
    }

    // Submit Nomination with Duplicate & Quota Checks
    public Nomination submitNomination(Long programId, Long officerId) {
        // 1. Check if Officer already nominated for this programme (Prevent Duplicate)
        if (nominationRepository.existsByProgrammeProgramIdAndOfficerOfficerId(programId, officerId)) {
            throw new RuntimeException("Officer is already nominated for this training programme.");
        }

        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        TrainingProgramme programme = programmeRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Programme not found"));

        // 2. Check Department Quota Limit
        Long deptId = officer.getDepartment().getDeptId();
        long currentDeptCount = nominationRepository.findAll().stream()
                .filter(n -> n.getProgramme().getProgramId().equals(programId) &&
                             n.getOfficer().getDepartment().getDeptId().equals(deptId))
                .count();

        if (currentDeptCount >= DEFAULT_DEPT_QUOTA) {
            throw new RuntimeException("Quota Limit Reached: Department cannot exceed " + DEFAULT_DEPT_QUOTA + " seats.");
        }

        Nomination nomination = new Nomination();
        nomination.setProgramme(programme);
        nomination.setOfficer(officer);
        nomination.setStatus("SUBMITTED");

        return nominationRepository.save(nomination);
    }

    public Nomination approveNomination(Long id) {
        Nomination nomination = nominationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nomination not found"));
        nomination.setStatus("APPROVED");
        return nominationRepository.save(nomination);
    }
}
