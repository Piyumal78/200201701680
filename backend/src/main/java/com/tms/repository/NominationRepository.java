package com.tms.repository;

import com.tms.entity.Nomination;
import com.tms.entity.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {

    // 1. Count confirmed nominations for a programme
    long countByProgrammeAndStatus(TrainingProgramme programme, String status);

    // 2. FIFO order: find first waiting nomination
    Nomination findFirstByProgrammeAndStatusOrderByNominatedAtAsc(TrainingProgramme programme, String status);

    // 3. Find all nominations for a programme ordered by nominatedAt
    List<Nomination> findByProgrammeOrderByNominatedAtAsc(TrainingProgramme programme);
}
