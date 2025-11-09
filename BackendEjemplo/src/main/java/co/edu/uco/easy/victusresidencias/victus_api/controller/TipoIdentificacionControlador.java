package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.TipoIdentificacionResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.TipoIdentificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/tipos-identificacion")
public class TipoIdentificacionControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(TipoIdentificacionControlador.class);
    private static final String NAME_CLASS_SINGULAR = "Tipo de Identificación";
    private static final String NAME_CLASS_PLURAL = "Tipos de Identificación";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró un %s con el ID especificado.";

    @Autowired
    private TipoIdentificacionService tipoIdentificacionService;

    @GetMapping("/dummy")
    public TipoIdentificacionEntity getDummy() {
        return new TipoIdentificacionEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<TipoIdentificacionResponse> retrieveAll() {
        var response = new TipoIdentificacionResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = tipoIdentificacionService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar los tipos de identificación", e);
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponse> retrieveById(@PathVariable UUID id) {
        var response = new TipoIdentificacionResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = tipoIdentificacionService.findById(id);
            if (entity.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.setData(List.of(entity.get()));
            messages.add(String.format("El %s fue consultado satisfactoriamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar el tipo de identificación", e);
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody TipoIdentificacionEntity tipoIdentificacion) {
        var messages = new ArrayList<String>();

        try {
            if (tipoIdentificacion.getNombre() == null || tipoIdentificacion.getNombre().isEmpty()) {
                String userMessage = String.format("El nombre del %s es requerido para poder registrar la información.",
                        NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El nombre del %s en la clase %s llegó nulo o vacío.", NAME_CLASS_SINGULAR,
                        TipoIdentificacionEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (tipoIdentificacionService.existsByNombre(tipoIdentificacion.getNombre())) {
                String userMessage = String.format("El %s ya existe", NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El %s con el nombre '", NAME_CLASS_SINGULAR)
                        + tipoIdentificacion.getNombre() + "' ya existe en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            tipoIdentificacionService.save(tipoIdentificacion);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear tipo de identificación", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar el %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear tipo de identificación", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponse> update(@PathVariable UUID id,
            @RequestBody TipoIdentificacionEntity tipoIdentificacion) {
        var response = new TipoIdentificacionResponse();
        var messages = new ArrayList<String>();

        try {
            var existingTipoIdentificacion = tipoIdentificacionService.findById(id);
            if (existingTipoIdentificacion.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            var updatedTipoIdentificacion = existingTipoIdentificacion.get();
            if (tipoIdentificacion.getNombre() != null && !tipoIdentificacion.getNombre().isEmpty()) {
                updatedTipoIdentificacion.setNombre(tipoIdentificacion.getNombre());
            }

            tipoIdentificacionService.save(updatedTipoIdentificacion);

            messages.add(String.format("El %s %s se actualizó correctamente.", NAME_CLASS_SINGULAR, updatedTipoIdentificacion.getNombre()));
            response.setData(List.of(updatedTipoIdentificacion));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error al actualizar tipo de identificación", e);
            messages.add(String.format("Error al actualizar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable UUID id) {
        var messages = new ArrayList<String>();
        var response = new GenericResponse();

        try {
            if (id == null) {
                var userMessage = String.format("El ID del %s es requerido para poder eliminar la información.",
                        NAME_CLASS_SINGULAR);
                var technicalMessage = String.format("El ID del %s en la clase TipoIdentificacionEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingTipoIdentificacion = tipoIdentificacionService.findById(id);
            if (existingTipoIdentificacion.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            tipoIdentificacionService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el tipo de identificación", e);
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}