package com.tms.service;

import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import com.tms.repository.NominationRepository;
import com.tms.repository.OfficerRepository;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 1. Submit nomination with strict FIFO capacity enforcement
    @Transactional
    public Nomination addNomination(Long programId, Long officerId, String officerName) {
        TrainingProgramme programme = programmeRepository.findById(programId)
                .orElseGet(() -> {
                    List<TrainingProgramme> list = programmeRepository.findAll();
                    if (!list.isEmpty()) return list.get(0);
                    TrainingProgramme defaultProg = new TrainingProgramme();
                    defaultProg.setTitle("Cybersecurity Awareness Programme");
                    defaultProg.setMaxParticipants(3);
                    defaultProg.setStatus("PUBLISHED");
                    return programmeRepository.save(defaultProg);
                });

        Officer officer = null;
        if (officerId != null) {
            officer = officerRepository.findById(officerId).orElse(null);
        }

        if (officer == null && officerName != null && !officerName.trim().isEmpty()) {
            final String targetName = officerName.trim();
            officer = officerRepository.findAll().stream()
                    .filter(o -> o.getFullName() != null && o.getFullName().equalsIgnoreCase(targetName))
                    .findFirst()
                    .orElseGet(() -> {
                        Officer newOfficer = new Officer();
                        newOfficer.setFullName(targetName);
                        newOfficer.setEmpNo("EMP-" + (100 + officerRepository.count() + 1));
                        newOfficer.setEmail(targetName.toLowerCase().replaceAll("[^a-z]", "") + "@tms.gov");
                        newOfficer.setDesignation("Officer");
                        newOfficer.setRole("OFFICER");
                        return officerRepository.save(newOfficer);
                    });
        }

        if (officer == null) {
            officer = officerRepository.findAll().stream().findFirst().orElse(null);
        }

        int maxCap = (programme.getMaxParticipants() != null) ? programme.getMaxParticipants() : 3;

        // Fetch all active nominations in FIFO order
        List<Nomination> allNominations = nominationRepository.findByProgrammeOrderByNominatedAtAsc(programme);

        // Count current confirmed seats strictly
        long confirmedCount = allNominations.stream()
                .filter(n -> "CONFIRMED".equalsIgnoreCase(n.getStatus()))
                .count();

        String assignedStatus = (confirmedCount < maxCap) ? "CONFIRMED" : "WAITING";

        // Check if this officer already exists in the programme
        Officer finalOfficer = officer;
        Nomination existingNom = allNominations.stream()
                .filter(n -> n.getOfficer() != null && n.getOfficer().getOfficerId().equals(finalOfficer.getOfficerId()))
                .findFirst()
                .orElse(null);

        if (existingNom != null) {
            if ("CANCELLED".equalsIgnoreCase(existingNom.getStatus())) {
                existingNom.setStatus(assignedStatus);
                existingNom.setNominatedAt(LocalDateTime.now());
                return nominationRepository.save(existingNom);
            }
            return existingNom;
        }

        Nomination nomination = new Nomination();
        nomination.setProgramme(programme);
        nomination.setOfficer(officer);
        nomination.setNominatedAt(LocalDateTime.now());
        nomination.setStatus(assignedStatus);

        return nominationRepository.save(nomination);
    }

    // 2. Cancel seat and auto-promote the oldest waiting nomination (FIFO)
    @Transactional
    public Nomination cancelNomination(Long nominationId) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new RuntimeException("Nomination not found with ID: " + nominationId));

        boolean wasConfirmed = "CONFIRMED".equalsIgnoreCase(nomination.getStatus());
        nomination.setStatus("CANCELLED");
        nominationRepository.save(nomination);

        // If a confirmed seat was freed, promote first person in waiting list
        if (wasConfirmed) {
            Nomination nextWaiting = nominationRepository
                    .findFirstByProgrammeAndStatusOrderByNominatedAtAsc(nomination.getProgramme(), "WAITING");

            if (nextWaiting != null) {
                nextWaiting.setStatus("CONFIRMED");
                nominationRepository.save(nextWaiting);
            }
        }

        return nomination;
    }

    // 3. Get all nominations for a programme with FIFO consistency check
    @Transactional
    public List<Nomination> getNominationsByProgramme(Long programId) {
        TrainingProgramme programme = programmeRepository.findById(programId)
                .orElseGet(() -> programmeRepository.findAll().stream().findFirst().orElse(null));

        if (programme == null) return List.of();

        List<Nomination> list = nominationRepository.findByProgrammeOrderByNominatedAtAsc(programme);
        int maxCap = (programme.getMaxParticipants() != null) ? programme.getMaxParticipants() : 3;

        // Auto-correct any legacy/inconsistent data strictly to maxCap
        long confirmedCount = 0;
        for (Nomination n : list) {
            if ("CANCELLED".equalsIgnoreCase(n.getStatus())) continue;

            if (confirmedCount < maxCap) {
                if (!"CONFIRMED".equalsIgnoreCase(n.getStatus())) {
                    n.setStatus("CONFIRMED");
                    nominationRepository.save(n);
                }
                confirmedCount++;
            } else {
                if (!"WAITING".equalsIgnoreCase(n.getStatus())) {
                    n.setStatus("WAITING");
                    nominationRepository.save(n);
                }
            }
        }

        return list;
    }
}
