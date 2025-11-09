package co.edu.uco.backendvictus.infrastructure.primary.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateRequest;
import co.edu.uco.backendvictus.application.usecase.conjunto.CreateConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.DeleteConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.ListConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.UpdateConjuntoUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.DataSanitizer;

@RestController
@RequestMapping("/conjuntos")
public class ConjuntoResidencialController {

    private final CreateConjuntoUseCase createConjuntoUseCase;
    private final ListConjuntoUseCase listConjuntoUseCase;
    private final UpdateConjuntoUseCase updateConjuntoUseCase;
    private final DeleteConjuntoUseCase deleteConjuntoUseCase;

    public ConjuntoResidencialController(final CreateConjuntoUseCase createConjuntoUseCase,
            final ListConjuntoUseCase listConjuntoUseCase, final UpdateConjuntoUseCase updateConjuntoUseCase,
            final DeleteConjuntoUseCase deleteConjuntoUseCase) {
        this.createConjuntoUseCase = createConjuntoUseCase;
        this.listConjuntoUseCase = listConjuntoUseCase;
        this.updateConjuntoUseCase = updateConjuntoUseCase;
        this.deleteConjuntoUseCase = deleteConjuntoUseCase;
    }

    @PostMapping
    public ResponseEntity<ConjuntoResponse> crear(@RequestBody final ConjuntoCreateRequest request) {
        final ConjuntoCreateRequest sanitized = new ConjuntoCreateRequest(request.ciudadId(),
                request.administradorId(), DataSanitizer.sanitizeText(request.nombre()),
                DataSanitizer.sanitizeText(request.direccion()), request.activo());
        final ConjuntoResponse response = createConjuntoUseCase.execute(sanitized);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ConjuntoResponse> listar() {
        return listConjuntoUseCase.execute();
    }

    @PutMapping("/{id}")
    public ConjuntoResponse actualizar(@PathVariable("id") final UUID id,
            @RequestBody final ConjuntoUpdateRequest request) {
        final ConjuntoUpdateRequest sanitized = new ConjuntoUpdateRequest(id, request.ciudadId(),
                request.administradorId(), DataSanitizer.sanitizeText(request.nombre()),
                DataSanitizer.sanitizeText(request.direccion()), request.activo());
        return updateConjuntoUseCase.execute(sanitized);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") final UUID id) {
        deleteConjuntoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
