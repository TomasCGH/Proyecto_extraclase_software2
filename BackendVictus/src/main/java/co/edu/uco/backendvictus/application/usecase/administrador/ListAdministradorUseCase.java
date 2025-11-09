package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;

@Service
public class ListAdministradorUseCase {

    private final AdministradorRepository administradorRepository;

    public ListAdministradorUseCase(final AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorResponse> execute() {
        return AdministradorApplicationMapper.toResponseList(administradorRepository.findAll());
    }
}
