package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;

@Service
public class DeleteAdministradorUseCase {

    private final AdministradorRepository administradorRepository;

    public DeleteAdministradorUseCase(final AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Transactional
    public void execute(final UUID id) {
        administradorRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Administrador no encontrado"));
        administradorRepository.deleteById(id);
    }
}
