package co.edu.uco.backendvictus.application.usecase.pais;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.domain.port.PaisRepository;

@Service
public class ListPaisUseCase {

    private final PaisRepository repository;

    public ListPaisUseCase(final PaisRepository repository) {
        this.repository = repository;
    }

    public List<PaisResponse> execute() {
        return PaisApplicationMapper.toResponseList(repository.findAll());
    }
}
