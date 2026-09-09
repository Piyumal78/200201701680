package com.tms.controller;

import com.tms.entity.TrainingProgramme;
import com.tms.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
@CrossOrigin(origins = "*")
public class ProgrammeController {

    @Autowired
    private ProgrammeService programmeService;

    @GetMapping
    public List<TrainingProgramme> getAllProgrammes() {
        return programmeService.getAllProgrammes();
    }

    @PostMapping
    public TrainingProgramme createProgramme(@RequestBody TrainingProgramme programme) {
        return programmeService.saveProgramme(programme);
    }
}
