package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;

import java.util.List;

public final class CiudadResponse extends ResponseWithData<CiudadEntity> {
    public static final Response build(final List<String> messages, final List<CiudadEntity> data) {
        var response = new CiudadResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}