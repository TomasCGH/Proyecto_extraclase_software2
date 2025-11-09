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

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoCreateRequest;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoUpdateRequest;
import co.edu.uco.backendvictus.application.usecase.departamento.CreateDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.DeleteDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.ListDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.UpdateDepartamentoUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.DataSanitizer;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final CreateDepartamentoUseCase createDepartamentoUseCase;
    private final ListDepartamentoUseCase listDepartamentoUseCase;
    private final UpdateDepartamentoUseCase updateDepartamentoUseCase;
    private final DeleteDepartamentoUseCase deleteDepartamentoUseCase;

    public DepartamentoController(final CreateDepartamentoUseCase createDepartamentoUseCase,
            final ListDepartamentoUseCase listDepartamentoUseCase,
            final UpdateDepartamentoUseCase updateDepartamentoUseCase,
            final DeleteDepartamentoUseCase deleteDepartamentoUseCase) {
        this.createDepartamentoUseCase = createDepartamentoUseCase;
        this.listDepartamentoUseCase = listDepartamentoUseCase;
        this.updateDepartamentoUseCase = updateDepartamentoUseCase;
        this.deleteDepartamentoUseCase = deleteDepartamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> crear(@RequestBody final DepartamentoCreateRequest request) {
        final DepartamentoCreateRequest sanitized = new DepartamentoCreateRequest(request.paisId(),
                DataSanitizer.sanitizeText(request.nombre()), request.activo());
        final DepartamentoResponse response = createDepartamentoUseCase.execute(sanitized);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<DepartamentoResponse> listar() {
        return listDepartamentoUseCase.execute();
    }

    @PutMapping("/{id}")
    public DepartamentoResponse actualizar(@PathVariable("id") final UUID id,
            @RequestBody final DepartamentoUpdateRequest request) {
        final DepartamentoUpdateRequest sanitized = new DepartamentoUpdateRequest(id, request.paisId(),
                DataSanitizer.sanitizeText(request.nombre()), request.activo());
        return updateDepartamentoUseCase.execute(sanitized);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") final UUID id) {
        deleteDepartamentoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
