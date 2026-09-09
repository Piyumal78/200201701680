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

    public TrainingProgramme saveProgramme(TrainingProgramme programme) {
        return programmeRepository.save(programme);
    }
}
