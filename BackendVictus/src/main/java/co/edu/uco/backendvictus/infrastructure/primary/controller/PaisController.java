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

import co.edu.uco.backendvictus.application.dto.pais.PaisCreateRequest;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateRequest;
import co.edu.uco.backendvictus.application.usecase.pais.CreatePaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.DeletePaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.ListPaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.UpdatePaisUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.DataSanitizer;

@RestController
@RequestMapping("/paises")
public class PaisController {

    private final CreatePaisUseCase createPaisUseCase;
    private final ListPaisUseCase listPaisUseCase;
    private final UpdatePaisUseCase updatePaisUseCase;
    private final DeletePaisUseCase deletePaisUseCase;

    public PaisController(final CreatePaisUseCase createPaisUseCase, final ListPaisUseCase listPaisUseCase,
            final UpdatePaisUseCase updatePaisUseCase, final DeletePaisUseCase deletePaisUseCase) {
        this.createPaisUseCase = createPaisUseCase;
        this.listPaisUseCase = listPaisUseCase;
        this.updatePaisUseCase = updatePaisUseCase;
        this.deletePaisUseCase = deletePaisUseCase;
    }

    @PostMapping
    public ResponseEntity<PaisResponse> crear(@RequestBody final PaisCreateRequest request) {
        final PaisCreateRequest sanitized = new PaisCreateRequest(DataSanitizer.sanitizeText(request.nombre()),
                request.activo());
        final PaisResponse response = createPaisUseCase.execute(sanitized);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<PaisResponse> listar() {
        return listPaisUseCase.execute();
    }

    @PutMapping("/{id}")
    public PaisResponse actualizar(@PathVariable("id") final UUID id, @RequestBody final PaisUpdateRequest request) {
        final PaisUpdateRequest sanitized = new PaisUpdateRequest(id, DataSanitizer.sanitizeText(request.nombre()),
                request.activo());
        return updatePaisUseCase.execute(sanitized);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") final UUID id) {
        deletePaisUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
