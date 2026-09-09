package com.tms.repository;

import com.tms.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    List<Officer> findByDepartmentDeptId(Long deptId);
}
