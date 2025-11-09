package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;

@Service
public class DeleteDepartamentoUseCase {

    private final DepartamentoRepository departamentoRepository;

    public DeleteDepartamentoUseCase(final DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Transactional
    public void execute(final UUID id) {
        departamentoRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Departamento no encontrado"));
        departamentoRepository.deleteById(id);
    }
}
