package com.example.ParcialDs.Controller;

import com.example.ParcialDs.Service.MutantService;
import com.example.ParcialDs.Entity.Mutant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")  // Permite solicitudes de cualquier origen
@RequestMapping("/api/mutant/")
public class MutantController {

    @Autowired
    private MutantService mutantService;


    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Mutant entity) {
        try {
            Mutant entidad = mutantService.save(entity);
            if (entidad.getEsMutant()) {
                return ResponseEntity.status(HttpStatus.OK).body("Efectivamente es mutante");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El ADN ingresado no corresponde con el de un mutante");
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El ADN está vacío");
        } catch (ArrayIndexOutOfBoundsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El ADN no tiene un tamaño de NxN");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Alguna de las letras del ADN no corresponde con una de las válidas");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("EL ADN ingresado ya se encuentra en la base de datos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"Error, por favor intente más tarde\"}");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = mutantService.getStatistics(); // Asegúrate de que este método esté en tu servicio
        return ResponseEntity.ok(stats);
    }
}
