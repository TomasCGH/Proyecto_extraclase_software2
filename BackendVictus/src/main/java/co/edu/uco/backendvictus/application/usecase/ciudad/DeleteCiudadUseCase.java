package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;

@Service
public class DeleteCiudadUseCase {

    private final CiudadRepository ciudadRepository;

    public DeleteCiudadUseCase(final CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    @Transactional
    public void execute(final UUID id) {
        ciudadRepository.findById(id).orElseThrow(() -> new ApplicationException("Ciudad no encontrada"));
        ciudadRepository.deleteById(id);
    }
}
