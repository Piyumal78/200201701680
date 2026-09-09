package com.tms.repository;

import com.tms.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {
    
    // Check if officer is already nominated for this programme
    boolean existsByProgrammeProgramIdAndOfficerOfficerId(Long programId, Long officerId);
}
