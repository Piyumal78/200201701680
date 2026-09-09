package com.tms.service;

import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NominationService {

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private TrainingProgrammeRepository programmeRepository;

    @Autowired
    private OfficerRepository officerRepository;

    public Nomination createNomination(Long programId, Long officerId) {
        TrainingProgramme programme = programmeRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Programme not found with ID: " + programId));
        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found with ID: " + officerId));

        Nomination nomination = new Nomination(programme, officer, LocalDateTime.now(), "CONFIRMED");
        return nominationRepository.save(nomination);
    }

    public List<Nomination> getAllNominations() {
        return nominationRepository.findAll();
    }
}
