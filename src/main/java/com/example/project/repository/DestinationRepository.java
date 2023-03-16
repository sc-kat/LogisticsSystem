package com.example.project.repository;

import com.example.project.entity.DestinationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationRepository extends CrudRepository<DestinationEntity, Long> {

    @Query(value = "SELECT d FROM destinations d WHERE d.name = :name")
    Optional<DestinationEntity> findByName(@Param("name") String name);


}
