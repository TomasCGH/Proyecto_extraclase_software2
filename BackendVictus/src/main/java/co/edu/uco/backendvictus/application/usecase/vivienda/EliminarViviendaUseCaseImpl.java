package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import reactor.core.publisher.Mono;

@Service
public class EliminarViviendaUseCaseImpl implements EliminarViviendaUseCase {

    private final ViviendaRepository viviendaRepository;

    public EliminarViviendaUseCaseImpl(final ViviendaRepository viviendaRepository) {
        this.viviendaRepository = viviendaRepository;
    }

    @Override
    public Mono<Void> execute(final UUID viviendaId) {
        return viviendaRepository.deleteById(viviendaId);
    }
}
