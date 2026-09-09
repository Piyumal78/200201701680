package com.tms.controller;

import com.tms.entity.Nomination;
import com.tms.service.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nominations")
@CrossOrigin(origins = "*")
public class NominationController {

    @Autowired
    private NominationService nominationService;

    // POST /api/nominations/submit
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitNomination(@RequestBody Map<String, Object> payload) {
        Long programId = payload.get("programId") != null ? Long.valueOf(payload.get("programId").toString()) : 1L;
        Long officerId = payload.get("officerId") != null ? Long.valueOf(payload.get("officerId").toString()) : null;
        String officerName = payload.get("officerName") != null ? payload.get("officerName").toString() : null;

        Nomination result = nominationService.addNomination(programId, officerId, officerName);
        return ResponseEntity.ok(formatNomination(result));
    }

    // PUT /api/nominations/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelNomination(@PathVariable Long id) {
        Nomination result = nominationService.cancelNomination(id);
        return ResponseEntity.ok(formatNomination(result));
    }

    // GET /api/nominations/programme/{programId}
    @GetMapping("/programme/{programId}")
    public ResponseEntity<List<Map<String, Object>>> getNominations(@PathVariable Long programId) {
        List<Nomination> list = nominationService.getNominationsByProgramme(programId);
        List<Map<String, Object>> response = list.stream().map(this::formatNomination).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> formatNomination(Nomination nom) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nom.getNominationId());
        map.put("status", nom.getStatus());
        map.put("nominatedAt", nom.getNominatedAt() != null ? nom.getNominatedAt().toString() : "");
        map.put("officerName", nom.getOfficer() != null ? nom.getOfficer().getFullName() : "Officer");
        map.put("empNo", nom.getOfficer() != null ? nom.getOfficer().getEmpNo() : "");
        map.put("department", (nom.getOfficer() != null && nom.getOfficer().getDepartment() != null) 
                ? nom.getOfficer().getDepartment().getDeptCode() : "GEN");
        return map;
    }
}
