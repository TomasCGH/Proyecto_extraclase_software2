package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;

import java.util.List;

public final class DepartamentoResponse extends ResponseWithData<DepartamentoEntity> {
    public static final Response build(final List<String> messages, final List<DepartamentoEntity> data) {
        var response = new DepartamentoResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}