package co.edu.uco.backendvictus.application.mapper;

import java.util.List;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.domain.model.Departamento;

public final class DepartamentoApplicationMapper {

    private DepartamentoApplicationMapper() {
    }

    public static DepartamentoResponse toResponse(final Departamento departamento) {
        return new DepartamentoResponse(departamento.getId(), departamento.getPais().getId(),
                departamento.getNombre(), departamento.isActivo());
    }

    public static List<DepartamentoResponse> toResponseList(final List<Departamento> departamentos) {
        return departamentos.stream().map(DepartamentoApplicationMapper::toResponse).toList();
    }
}
