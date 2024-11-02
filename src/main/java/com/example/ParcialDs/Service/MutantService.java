package com.example.ParcialDs.Service;

import com.example.ParcialDs.Entity.Mutant;
import com.example.ParcialDs.Repository.DnaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MutantService {

    private static final int SEQUENCE_LENGTH = 4;

    @Autowired
    private DnaRepository dnaRepository;

    @Transactional
    public Mutant save(Mutant entity) throws Exception {
        entity.setEsMutant(this.checkIfMutant(entity.getDna()));
        entity = dnaRepository.save(entity);  // Usa la instancia inyectada "dnaRepository" en minúscula
        return entity;
    }

    public boolean checkIfMutant(String[] dna) {
        if (dna.length == 0) {
            throw new NullPointerException("El ADN está vacío.");
        }

        String validBases = "ATCG";
        for (String strand : dna) {
            if (strand.length() != dna.length) {
                throw new ArrayIndexOutOfBoundsException("Las longitudes de las cadenas de ADN no coinciden.");
            }
            for (char base : strand.toCharArray()) {
                if (validBases.indexOf(base) == -1) {
                    throw new IllegalArgumentException("Base de ADN no válida: " + base);
                }
            }
        }

        int sequenceCount = countSequences(dna);
        return sequenceCount > 1;
    }


    private int countSequences(String[] dna) {
        int count = 0;
        int n = dna.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (hasHorizontalSequence(dna, i, j, n) ||
                        hasVerticalSequence(dna, i, j, n) ||
                        hasDiagonalSequence(dna, i, j, n)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean hasHorizontalSequence(String[] dna, int i, int j, int n) {
        if (j + SEQUENCE_LENGTH > n) return false;
        char base = dna[i].charAt(j);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[i].charAt(j + k) != base) return false;
        }
        return true;
    }

    private boolean hasVerticalSequence(String[] dna, int i, int j, int n) {
        if (i + SEQUENCE_LENGTH > n) return false;
        char base = dna[i].charAt(j);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[i + k].charAt(j) != base) return false;
        }
        return true;
    }

    private boolean hasDiagonalSequence(String[] dna, int i, int j, int n) {
        if (i + SEQUENCE_LENGTH > n || j + SEQUENCE_LENGTH > n) return false;
        char base = dna[i].charAt(j);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[i + k].charAt(j + k) != base) return false;
        }
        return true;
    }

    @Transactional
    public Map<String, Object> getStatistics() {
        long mutantCount = dnaRepository.countByEsMutantTrue();
        long humanCount = dnaRepository.countByEsMutantFalse();

        Map<String, Object> stats = new HashMap<>();
        stats.put("count_mutant_dna", mutantCount);
        stats.put("count_human_dna", humanCount);
        stats.put("ratio", humanCount == 0 ? 0 : (double) mutantCount / humanCount);

        return stats;
    }
}
