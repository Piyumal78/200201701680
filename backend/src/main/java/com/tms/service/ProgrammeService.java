package com.tms.service;

import com.tms.entity.TrainingProgramme;
import com.tms.repository.TrainingProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgrammeService {

    @Autowired
    private TrainingProgrammeRepository programmeRepository;

    public List<TrainingProgramme> getAllProgrammes() {
        return programmeRepository.findAll();
    }

    // Simple Conflict Detection Method
    public TrainingProgramme saveProgramme(TrainingProgramme newProg) {
        List<TrainingProgramme> existingList = programmeRepository.findAll();

        for (TrainingProgramme p : existingList) {
            if ("CANCELLED".equals(p.getStatus())) continue;

            // Check if date and time overlaps
            boolean dateOverlap = !(newProg.getEndDate().isBefore(p.getStartDate()) || newProg.getStartDate().isAfter(p.getEndDate()));
            boolean timeOverlap = !(newProg.getEndTime().isBefore(p.getStartTime()) || newProg.getStartTime().isAfter(p.getEndTime()));

            if (dateOverlap && timeOverlap) {
                // Check Venue Conflict
                if (newProg.getVenue() != null && newProg.getVenue().getVenueId().equals(p.getVenue().getVenueId())) {
                    throw new RuntimeException("Venue Conflict: Selected venue is already booked for " + p.getTitle());
                }

                // Check Trainer Conflict
                if (newProg.getTrainer() != null && newProg.getTrainer().getTrainerId().equals(p.getTrainer().getTrainerId())) {
                    throw new RuntimeException("Trainer Conflict: Selected trainer is already busy with " + p.getTitle());
                }
            }
        }

        newProg.setStatus("PUBLISHED");
        return programmeRepository.save(newProg);
    }
}
