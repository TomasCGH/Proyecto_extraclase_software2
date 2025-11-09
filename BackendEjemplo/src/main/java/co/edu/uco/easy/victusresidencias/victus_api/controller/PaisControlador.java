package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.PaisResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.PaisService;
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
@RequestMapping("/api/v1/paises")
public class PaisControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaisControlador.class);
    private static final String NAME_CLASS_SINGULAR = "País";
    private static final String NAME_CLASS_PLURAL = "Países";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró un %s con el ID especificado.";

    @Autowired
    private PaisService paisService;

    @GetMapping("/dummy")
    public PaisEntity getDummy() {
        return new PaisEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<PaisResponse> retrieveAll() {
        var response = new PaisResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = paisService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar los países", e);
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisResponse> retrieveById(@PathVariable UUID id) {
        var response = new PaisResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = paisService.findById(id);
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
            LOGGER.error("Error al consultar el país", e);
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody PaisEntity pais) {
        var messages = new ArrayList<String>();

        try {
            if (pais.getNombre() == null || pais.getNombre().isEmpty()) {
                String userMessage = String.format("El nombre del %s es requerido para poder registrar la información.",
                        NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El nombre del %s en la clase %s llegó nulo o vacío.", NAME_CLASS_SINGULAR,
                        PaisEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (paisService.existsByNombre(pais.getNombre())) {
                String userMessage = String.format("El %s ya existe", NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El %s con el nombre '", NAME_CLASS_SINGULAR)
                        + pais.getNombre() + "' ya existe en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            paisService.save(pais);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear país", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar el %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear país", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisResponse> update(@PathVariable UUID id,
            @RequestBody PaisEntity pais) {
        var response = new PaisResponse();
        var messages = new ArrayList<String>();

        try {
            var existingPais = paisService.findById(id);
            if (existingPais.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            var updatedPais = existingPais.get();
            if (pais.getNombre() != null && !pais.getNombre().isEmpty()) {
                updatedPais.setNombre(pais.getNombre());
            }

            paisService.save(updatedPais);

            messages.add(String.format("El %s %s se actualizó correctamente.", NAME_CLASS_SINGULAR, updatedPais.getNombre()));
            response.setData(List.of(updatedPais));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error al actualizar país", e);
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
                var technicalMessage = String.format("El ID del %s en la clase PaisEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingPais = paisService.findById(id);
            if (existingPais.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            paisService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el país", e);
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}