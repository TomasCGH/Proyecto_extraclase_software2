package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.ObjectHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;

public class UcoApplicationException extends RuntimeException {

	private static final long serialVersionUID = -7682900863325465078L;

	private final String userMessage;
	private final Layer layer;

	public UcoApplicationException(final String userMessage, final String technicalMessage,
			final Exception rootException, final Layer layer) {

		super(TextHelper.applyTrim(technicalMessage), ObjectHelper.getDefault(rootException, new Exception()));

		this.userMessage = TextHelper.applyTrim(userMessage);
		this.layer = ObjectHelper.getDefault(layer, Layer.GENERAL);
	}

	public String getUserMessage() {
		return userMessage;
	}

	public Layer getLayer() {
		return layer;
	}
}