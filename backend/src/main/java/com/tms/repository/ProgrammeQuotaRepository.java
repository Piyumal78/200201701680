package com.tms.repository;

import com.tms.entity.ProgrammeQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeQuotaRepository extends JpaRepository<ProgrammeQuota, Long> {
}
