package com.tms.repository;

import com.tms.entity.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TrainingProgrammeRepository extends JpaRepository<TrainingProgramme, Long> {

    // Venue overlap query: checks if venue is booked during overlapping date/time
    @Query("SELECT p FROM TrainingProgramme p WHERE p.venue.venueId = :venueId " +
           "AND p.status <> 'CANCELLED' " +
           "AND (:startDate <= p.endDate AND :endDate >= p.startDate) " +
           "AND (:startTime < p.endTime AND :endTime > p.startTime)")
    List<TrainingProgramme> findVenueConflicts(@Param("venueId") Long venueId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("startTime") LocalTime startTime,
                                                @Param("endTime") LocalTime endTime);

    // Trainer overlap query: checks if trainer is assigned during overlapping date/time
    @Query("SELECT p FROM TrainingProgramme p WHERE p.trainer.trainerId = :trainerId " +
           "AND p.status <> 'CANCELLED' " +
           "AND (:startDate <= p.endDate AND :endDate >= p.startDate) " +
           "AND (:startTime < p.endTime AND :endTime > p.startTime)")
    List<TrainingProgramme> findTrainerConflicts(@Param("trainerId") Long trainerId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);
}
