package co.edu.uco.backendvictus.application.mapper;

import java.util.List;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.domain.model.Ciudad;

public final class CiudadApplicationMapper {

    private CiudadApplicationMapper() {
    }

    public static CiudadResponse toResponse(final Ciudad ciudad) {
        return new CiudadResponse(ciudad.getId(), ciudad.getDepartamento().getId(), ciudad.getNombre(),
                ciudad.isActivo());
    }

    public static List<CiudadResponse> toResponseList(final List<Ciudad> ciudades) {
        return ciudades.stream().map(CiudadApplicationMapper::toResponse).toList();
    }
}
