package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nominations", uniqueConstraints = {
    @UniqueConstraint(name = "unique_officer_programme", columnNames = {"program_id", "officer_id"})
})
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nominationId;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TrainingProgramme programme;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private Officer officer;

    private LocalDateTime nominatedAt;
    private String status; // SUBMITTED, APPROVED, ATTENDED

    public Nomination() {
        this.nominatedAt = LocalDateTime.now();
        this.status = "SUBMITTED";
    }

    public Long getNominationId() { return nominationId; }
    public void setNominationId(Long nominationId) { this.nominationId = nominationId; }

    public TrainingProgramme getProgramme() { return programme; }
    public void setProgramme(TrainingProgramme programme) { this.programme = programme; }

    public Officer getOfficer() { return officer; }
    public void setOfficer(Officer officer) { this.officer = officer; }

    public LocalDateTime getNominatedAt() { return nominatedAt; }
    public void setNominatedAt(LocalDateTime nominatedAt) { this.nominatedAt = nominatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
