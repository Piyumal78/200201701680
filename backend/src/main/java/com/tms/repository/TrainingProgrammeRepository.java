package com.tms.repository;

import com.tms.entity.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgrammeRepository extends JpaRepository<TrainingProgramme, Long> {
}
