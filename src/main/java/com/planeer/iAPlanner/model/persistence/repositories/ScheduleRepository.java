package com.planeer.iAPlanner.model.persistence.repositories;

import com.planeer.iAPlanner.model.persistence.domains.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
}