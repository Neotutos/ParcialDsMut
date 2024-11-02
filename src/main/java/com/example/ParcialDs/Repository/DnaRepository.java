package com.example.ParcialDs.Repository;

import com.example.ParcialDs.Entity.Mutant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaRepository extends JpaRepository<Mutant, Long> {
    long countByEsMutantTrue();

    long countByEsMutantFalse();
}
