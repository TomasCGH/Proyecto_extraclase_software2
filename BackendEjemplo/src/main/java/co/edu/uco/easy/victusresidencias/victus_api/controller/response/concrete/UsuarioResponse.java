package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;

import java.util.List;

public final class UsuarioResponse extends ResponseWithData<UsuarioEntity> {
    public static final Response build(final List<String> messages, final List<UsuarioEntity> data) {
        var response = new UsuarioResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}