package com.tms.repository;

import com.tms.entity.Nomination;
import com.tms.entity.Officer;
import com.tms.entity.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {

    // Find the most recent participation of an officer in a specific programme
    Optional<Nomination> findTopByOfficerAndProgrammeOrderByParticipationDateDesc(Officer officer, TrainingProgramme programme);

    // Check if an officer participated in a programme after a specific cutoff date (e.g., within 12 months)
    boolean existsByOfficerAndProgrammeAndParticipationDateAfter(Officer officer, TrainingProgramme programme, LocalDateTime cutoffDate);

    // Find all participations by an officer
    List<Nomination> findByOfficerOrderByParticipationDateDesc(Officer officer);
}
