package com.tms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "programme_quotas")
public class ProgrammeQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long programId;
    private Long deptId;
    private Integer allocatedQuota;

    public ProgrammeQuota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public Integer getAllocatedQuota() { return allocatedQuota; }
    public void setAllocatedQuota(Integer allocatedQuota) { this.allocatedQuota = allocatedQuota; }
}
