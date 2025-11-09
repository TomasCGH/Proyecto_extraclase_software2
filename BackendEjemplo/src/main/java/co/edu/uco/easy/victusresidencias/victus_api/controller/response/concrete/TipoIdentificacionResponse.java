package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;

import java.util.List;

public final class TipoIdentificacionResponse extends ResponseWithData<TipoIdentificacionEntity> {
    public static final Response build(final List<String> messages, final List<TipoIdentificacionEntity> data) {
        var response = new TipoIdentificacionResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}