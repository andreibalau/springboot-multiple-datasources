package com.test.multipledatasources.repository.animal;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.multipledatasources.model.animal.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

}
