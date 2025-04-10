package com.planeer.iAPlanner.model.persistence.repositories;

import com.planeer.iAPlanner.model.persistence.domains.ParticipantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsRepository extends JpaRepository<ParticipantsEntity, Long> {
}