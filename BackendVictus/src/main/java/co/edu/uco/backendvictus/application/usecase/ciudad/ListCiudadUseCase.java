package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;

@Service
public class ListCiudadUseCase {

    private final CiudadRepository ciudadRepository;

    public ListCiudadUseCase(final CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    public List<CiudadResponse> execute() {
        return CiudadApplicationMapper.toResponseList(ciudadRepository.findAll());
    }
}
