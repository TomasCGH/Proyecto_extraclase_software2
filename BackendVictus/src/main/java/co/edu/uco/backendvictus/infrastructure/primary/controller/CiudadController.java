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

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadCreateRequest;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadUpdateRequest;
import co.edu.uco.backendvictus.application.usecase.ciudad.CreateCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.DeleteCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.ListCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.UpdateCiudadUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.DataSanitizer;

@RestController
@RequestMapping("/ciudades")
public class CiudadController {

    private final CreateCiudadUseCase createCiudadUseCase;
    private final ListCiudadUseCase listCiudadUseCase;
    private final UpdateCiudadUseCase updateCiudadUseCase;
    private final DeleteCiudadUseCase deleteCiudadUseCase;

    public CiudadController(final CreateCiudadUseCase createCiudadUseCase, final ListCiudadUseCase listCiudadUseCase,
            final UpdateCiudadUseCase updateCiudadUseCase, final DeleteCiudadUseCase deleteCiudadUseCase) {
        this.createCiudadUseCase = createCiudadUseCase;
        this.listCiudadUseCase = listCiudadUseCase;
        this.updateCiudadUseCase = updateCiudadUseCase;
        this.deleteCiudadUseCase = deleteCiudadUseCase;
    }

    @PostMapping
    public ResponseEntity<CiudadResponse> crear(@RequestBody final CiudadCreateRequest request) {
        final CiudadCreateRequest sanitized = new CiudadCreateRequest(request.departamentoId(),
                DataSanitizer.sanitizeText(request.nombre()), request.activo());
        final CiudadResponse response = createCiudadUseCase.execute(sanitized);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<CiudadResponse> listar() {
        return listCiudadUseCase.execute();
    }

    @PutMapping("/{id}")
    public CiudadResponse actualizar(@PathVariable("id") final UUID id, @RequestBody final CiudadUpdateRequest request) {
        final CiudadUpdateRequest sanitized = new CiudadUpdateRequest(id, request.departamentoId(),
                DataSanitizer.sanitizeText(request.nombre()), request.activo());
        return updateCiudadUseCase.execute(sanitized);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") final UUID id) {
        deleteCiudadUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
