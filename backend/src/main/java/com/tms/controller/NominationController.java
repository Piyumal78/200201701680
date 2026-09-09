package com.tms.controller;

import com.tms.entity.Nomination;
import com.tms.service.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nominations")
@CrossOrigin(origins = "*")
public class NominationController {

    @Autowired
    private NominationService nominationService;

    @GetMapping
    public List<Nomination> getAllNominations() {
        return nominationService.getAllNominations();
    }

    @PostMapping("/submit")
    public Nomination submitNomination(@RequestParam Long programId, @RequestParam Long officerId) {
        return nominationService.createNomination(programId, officerId);
    }
}
