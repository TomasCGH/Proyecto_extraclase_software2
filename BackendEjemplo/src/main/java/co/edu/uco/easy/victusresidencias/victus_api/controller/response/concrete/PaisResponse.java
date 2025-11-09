package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;

import java.util.List;

public final class PaisResponse extends ResponseWithData<PaisEntity> {
    public static final Response build(final List<String> messages, final List<PaisEntity> data) {
        var response = new PaisResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}