package com.tms.controller;

import com.tms.entity.Officer;
import com.tms.repository.OfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officers")
@CrossOrigin(origins = "*")
public class OfficerController {

    @Autowired
    private OfficerRepository officerRepository;

    @GetMapping
    public List<Officer> getAllOfficers() {
        return officerRepository.findAll();
    }
}
