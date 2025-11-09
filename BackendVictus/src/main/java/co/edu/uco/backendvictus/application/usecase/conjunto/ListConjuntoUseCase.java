package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;

@Service
public class ListConjuntoUseCase {

    private final ConjuntoResidencialRepository conjuntoRepository;

    public ListConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository) {
        this.conjuntoRepository = conjuntoRepository;
    }

    public List<ConjuntoResponse> execute() {
        return ConjuntoApplicationMapper.toResponseList(conjuntoRepository.findAll());
    }
}
