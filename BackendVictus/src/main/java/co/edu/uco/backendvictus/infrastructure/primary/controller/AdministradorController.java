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

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorCreateRequest;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateRequest;
import co.edu.uco.backendvictus.application.usecase.administrador.CreateAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.DeleteAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.ListAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.UpdateAdministradorUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.DataSanitizer;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final CreateAdministradorUseCase createAdministradorUseCase;
    private final ListAdministradorUseCase listAdministradorUseCase;
    private final UpdateAdministradorUseCase updateAdministradorUseCase;
    private final DeleteAdministradorUseCase deleteAdministradorUseCase;

    public AdministradorController(final CreateAdministradorUseCase createAdministradorUseCase,
            final ListAdministradorUseCase listAdministradorUseCase,
            final UpdateAdministradorUseCase updateAdministradorUseCase,
            final DeleteAdministradorUseCase deleteAdministradorUseCase) {
        this.createAdministradorUseCase = createAdministradorUseCase;
        this.listAdministradorUseCase = listAdministradorUseCase;
        this.updateAdministradorUseCase = updateAdministradorUseCase;
        this.deleteAdministradorUseCase = deleteAdministradorUseCase;
    }

    @PostMapping
    public ResponseEntity<AdministradorResponse> crear(@RequestBody final AdministradorCreateRequest request) {
        final AdministradorCreateRequest sanitized = new AdministradorCreateRequest(
                DataSanitizer.sanitizeText(request.primerNombre()),
                DataSanitizer.sanitizeText(request.segundoNombres()),
                DataSanitizer.sanitizeText(request.primerApellido()),
                DataSanitizer.sanitizeText(request.segundoApellido()), DataSanitizer.sanitizeText(request.email()),
                DataSanitizer.sanitizeText(request.telefono()), request.activo());
        final AdministradorResponse response = createAdministradorUseCase.execute(sanitized);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<AdministradorResponse> listar() {
        return listAdministradorUseCase.execute();
    }

    @PutMapping("/{id}")
    public AdministradorResponse actualizar(@PathVariable("id") final UUID id,
            @RequestBody final AdministradorUpdateRequest request) {
        final AdministradorUpdateRequest sanitized = new AdministradorUpdateRequest(id,
                DataSanitizer.sanitizeText(request.primerNombre()), DataSanitizer.sanitizeText(request.segundoNombres()),
                DataSanitizer.sanitizeText(request.primerApellido()),
                DataSanitizer.sanitizeText(request.segundoApellido()), DataSanitizer.sanitizeText(request.email()),
                DataSanitizer.sanitizeText(request.telefono()), request.activo());
        return updateAdministradorUseCase.execute(sanitized);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") final UUID id) {
        deleteAdministradorUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
