package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.CiudadResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.CiudadService;
import co.edu.uco.easy.victusresidencias.victus_api.service.DepartamentoService;
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
@RequestMapping("/api/v1/ciudades")
public class CiudadControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(CiudadControlador.class);
    private static final String NAME_CLASS_SINGULAR = "Ciudad";
    private static final String NAME_CLASS_PLURAL = "Ciudades";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró una %s con el ID especificado.";

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping("/dummy")
    public CiudadEntity getDummy() {
        return new CiudadEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<CiudadResponse> retrieveAll() {
        var response = new CiudadResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = ciudadService.findAll();
            response.setData(entities);
            messages.add(String.format("Las %s fueron consultadas satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar las ciudades", e);
            messages.add(String.format("Error al consultar las %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponse> retrieveById(@PathVariable UUID id) {
        var response = new CiudadResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = ciudadService.findById(id);
            if (entity.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.setData(List.of(entity.get()));
            messages.add(String.format("La %s fue consultada satisfactoriamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar la ciudad", e);
            messages.add(String.format("Error al consultar la %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody CiudadEntity ciudad) {
        var messages = new ArrayList<String>();

        try {
            if (ciudad.getNombre() == null || ciudad.getNombre().isEmpty()) {
                String userMessage = String.format("El nombre de la %s es requerido para poder registrar la información.",
                        NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El nombre de la %s en la clase %s llegó nulo o vacío.", NAME_CLASS_SINGULAR,
                        CiudadEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (ciudad.getDepartamento() == null || ciudad.getDepartamento().getId() == null) {
                String userMessage = "El departamento es requerido para poder registrar la ciudad.";
                String technicalMessage = "El departamento en la clase CiudadEntity llegó nulo o con ID nulo.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var departamento = departamentoService.findById(ciudad.getDepartamento().getId());
            if (departamento.isEmpty()) {
                String userMessage = "El departamento especificado no existe.";
                String technicalMessage = "No se encontró el departamento con el ID proporcionado.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (ciudadService.existsByNombreAndDepartamentoId(ciudad.getNombre(), ciudad.getDepartamento().getId())) {
                String userMessage = String.format("La %s ya existe en el departamento especificado", NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("La %s con el nombre '", NAME_CLASS_SINGULAR)
                        + ciudad.getNombre() + "' ya existe para el departamento con ID " + ciudad.getDepartamento().getId();
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            ciudad.setDepartamento(departamento.get());
            ciudadService.save(ciudad);
            messages.add(String.format("La %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear ciudad", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar la %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear ciudad", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponse> update(@PathVariable UUID id,
            @RequestBody CiudadEntity ciudad) {
        var response = new CiudadResponse();
        var messages = new ArrayList<String>();

        try {
            var existingCiudad = ciudadService.findById(id);
            if (existingCiudad.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            var updatedCiudad = existingCiudad.get();
            if (ciudad.getNombre() != null && !ciudad.getNombre().isEmpty()) {
                updatedCiudad.setNombre(ciudad.getNombre());
            }

            if (ciudad.getDepartamento() != null && ciudad.getDepartamento().getId() != null) {
                var newDepartamento = departamentoService.findById(ciudad.getDepartamento().getId());
                if (newDepartamento.isEmpty()) {
                    messages.add("El departamento especificado no existe.");
                    response.setMessages(messages);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                updatedCiudad.setDepartamento(newDepartamento.get());
            }

            ciudadService.save(updatedCiudad);

            messages.add(String.format("La %s %s se actualizó correctamente.", NAME_CLASS_SINGULAR, updatedCiudad.getNombre()));
            response.setData(List.of(updatedCiudad));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error al actualizar ciudad", e);
            messages.add(String.format("Error al actualizar la %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
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
                var userMessage = String.format("El ID de la %s es requerido para poder eliminar la información.",
                        NAME_CLASS_SINGULAR);
                var technicalMessage = String.format("El ID de la %s en la clase CiudadEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingCiudad = ciudadService.findById(id);
            if (existingCiudad.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            ciudadService.deleteById(id);
            messages.add(String.format("La %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar la ciudad", e);
            messages.add(String.format("Error al eliminar la %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}