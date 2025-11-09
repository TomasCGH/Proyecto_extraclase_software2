package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.DepartamentoResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.DepartamentoService;
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
@RequestMapping("/api/v1/departamentos")
public class DepartamentoControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoControlador.class);
    private static final String NAME_CLASS_SINGULAR = "Departamento";
    private static final String NAME_CLASS_PLURAL = "Departamentos";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró un %s con el ID especificado.";

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private PaisService paisService;

    @GetMapping("/dummy")
    public DepartamentoEntity getDummy() {
        return new DepartamentoEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<DepartamentoResponse> retrieveAll() {
        var response = new DepartamentoResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = departamentoService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar los departamentos", e);
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> retrieveById(@PathVariable UUID id) {
        var response = new DepartamentoResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = departamentoService.findById(id);
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
            LOGGER.error("Error al consultar el departamento", e);
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody DepartamentoEntity departamento) {
        var messages = new ArrayList<String>();

        try {
            if (departamento.getNombre() == null || departamento.getNombre().isEmpty()) {
                String userMessage = String.format("El nombre del %s es requerido para poder registrar la información.",
                        NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El nombre del %s en la clase %s llegó nulo o vacío.", NAME_CLASS_SINGULAR,
                        DepartamentoEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (departamento.getPais() == null || departamento.getPais().getId() == null) {
                String userMessage = "El país es requerido para poder registrar el departamento.";
                String technicalMessage = "El país en la clase DepartamentoEntity llegó nulo o con ID nulo.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var pais = paisService.findById(departamento.getPais().getId());
            if (pais.isEmpty()) {
                String userMessage = "El país especificado no existe.";
                String technicalMessage = "No se encontró el país con el ID proporcionado.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (departamentoService.existsByNombreAndPaisId(departamento.getNombre(), departamento.getPais().getId())) {
                String userMessage = String.format("El %s ya existe en el país especificado", NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El %s con el nombre '", NAME_CLASS_SINGULAR)
                        + departamento.getNombre() + "' ya existe para el país con ID " + departamento.getPais().getId();
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            departamento.setPais(pais.get());
            departamentoService.save(departamento);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear departamento", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar el %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear departamento", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> update(@PathVariable UUID id,
            @RequestBody DepartamentoEntity departamento) {
        var response = new DepartamentoResponse();
        var messages = new ArrayList<String>();

        try {
            var existingDepartamento = departamentoService.findById(id);
            if (existingDepartamento.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            var updatedDepartamento = existingDepartamento.get();
            if (departamento.getNombre() != null && !departamento.getNombre().isEmpty()) {
                updatedDepartamento.setNombre(departamento.getNombre());
            }

            if (departamento.getPais() != null && departamento.getPais().getId() != null) {
                var newPais = paisService.findById(departamento.getPais().getId());
                if (newPais.isEmpty()) {
                    messages.add("El país especificado no existe.");
                    response.setMessages(messages);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                updatedDepartamento.setPais(newPais.get());
            }

            departamentoService.save(updatedDepartamento);

            messages.add(String.format("El %s %s se actualizó correctamente.", NAME_CLASS_SINGULAR, updatedDepartamento.getNombre()));
            response.setData(List.of(updatedDepartamento));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error al actualizar departamento", e);
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
                var technicalMessage = String.format("El ID del %s en la clase DepartamentoEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingDepartamento = departamentoService.findById(id);
            if (existingDepartamento.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            departamentoService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el departamento", e);
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}