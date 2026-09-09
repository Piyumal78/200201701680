package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    private String title;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private Integer maxParticipants;
    private String status; // PUBLISHED, DRAFT, CANCELLED

    public TrainingProgramme() {}

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
