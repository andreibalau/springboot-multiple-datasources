package com.test.multipledatasources.repository.forest;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.multipledatasources.model.forest.Forest;

@Repository
public interface ForestRepository extends JpaRepository<Forest, UUID> {

}
