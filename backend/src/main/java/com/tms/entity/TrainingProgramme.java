package com.tms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    @JsonProperty("id")
    private Long id;

    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "programme_allowed_departments", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "department_name")
    private List<String> allowedDepartments = new ArrayList<>();

    private String requiredGrade;
    private Integer minimumYearsOfService;

    public TrainingProgramme() {}

    public TrainingProgramme(Long id, String title, List<String> allowedDepartments, String requiredGrade, Integer minimumYearsOfService) {
        this.id = id;
        this.title = title;
        this.allowedDepartments = allowedDepartments != null ? allowedDepartments : new ArrayList<>();
        this.requiredGrade = requiredGrade;
        this.minimumYearsOfService = minimumYearsOfService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAllowedDepartments() {
        return allowedDepartments;
    }

    public void setAllowedDepartments(List<String> allowedDepartments) {
        this.allowedDepartments = allowedDepartments;
    }

    public String getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(String requiredGrade) {
        this.requiredGrade = requiredGrade;
    }

    public Integer getMinimumYearsOfService() {
        return minimumYearsOfService;
    }

    public void setMinimumYearsOfService(Integer minimumYearsOfService) {
        this.minimumYearsOfService = minimumYearsOfService;
    }
}
