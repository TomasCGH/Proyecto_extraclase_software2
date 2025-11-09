package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;

@Service
public class ListDepartamentoUseCase {

    private final DepartamentoRepository departamentoRepository;

    public ListDepartamentoUseCase(final DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<DepartamentoResponse> execute() {
        return DepartamentoApplicationMapper.toResponseList(departamentoRepository.findAll());
    }
}
