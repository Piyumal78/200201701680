package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nominations")
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nomination_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private TrainingProgramme programme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "officer_id")
    private Officer officer;

    private LocalDateTime participationDate;
    private String status; // ATTENDED, CONFIRMED, COMPLETED

    public Nomination() {
        this.participationDate = LocalDateTime.now();
        this.status = "ATTENDED";
    }

    public Nomination(TrainingProgramme programme, Officer officer, LocalDateTime participationDate, String status) {
        this.programme = programme;
        this.officer = officer;
        this.participationDate = participationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingProgramme getProgramme() {
        return programme;
    }

    public void setProgramme(TrainingProgramme programme) {
        this.programme = programme;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }

    public LocalDateTime getParticipationDate() {
        return participationDate;
    }

    public void setParticipationDate(LocalDateTime participationDate) {
        this.participationDate = participationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
