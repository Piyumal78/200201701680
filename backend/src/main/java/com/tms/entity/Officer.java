package com.tms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "officers")
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "officer_id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "full_name")
    private String name;

    private String department;
    private String grade;
    private Integer yearsOfService;

    public Officer() {}

    public Officer(Long id, String name, String department, String grade, Integer yearsOfService) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.yearsOfService = yearsOfService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
    }
}
