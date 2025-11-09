package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.AdministratorResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@CrossOrigin(origins = "https://tangerine-profiterole-824fd8.netlify.app")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/administradores")
public class AdministradorControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministradorControlador.class);
    private static final String NAME_CLASS_SINGULAR = "Administrador";
    private static final String NAME_CLASS_PLURAL = "Administradores";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró un %s con el ID especificado.";

    @Autowired
    private AdministratorService adminService;

    @GetMapping("/dummy")
    public AdministratorEntity getDummy() {
        return new AdministratorEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<AdministratorResponse> retrieveAll() {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = adminService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar los administradores", e);
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponse> retrieveById(@PathVariable UUID id) {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = adminService.findById(id);
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
            LOGGER.error("Error al consultar el administrador", e);
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody AdministratorEntity administrador) {
        var messages = new ArrayList<String>();

        try {
            if (administrador.getName() == null || administrador.getName().isEmpty()) {
                String userMessage = String.format("El %s es requerido para poder registrar la información.",
                        NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El %s en la clase %s llegó nulo o vacío.", NAME_CLASS_SINGULAR,
                        AdministratorEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            if (adminService.existsByName(administrador.getName())) {
                String userMessage = String.format("El %s ya existe", NAME_CLASS_SINGULAR);
                String technicalMessage = String.format("El %s con el nombre ", NAME_CLASS_SINGULAR)
                        + administrador.getName() + "' ya existe en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            adminService.save(administrador);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear administrador", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar el %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear administrador", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorResponse> update(@PathVariable UUID id,
            @RequestBody AdministratorEntity administrador) {
        var responseWithData = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var existingAdmin = adminService.findById(id);
            if (existingAdmin.isEmpty()) {
                return buildNotFoundResponse(responseWithData, messages);
            }

            var updatedAdmin = updateAdministratorFields(existingAdmin.get(), administrador);

            adminService.save(updatedAdmin);

            messages.add(
                    String.format("El %s %s se actualizó correctamente.", NAME_CLASS_SINGULAR, updatedAdmin.getName()));
            responseWithData.setData(List.of(updatedAdmin));
            responseWithData.setMessages(messages);
            return new ResponseEntity<>(responseWithData, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error al actualizar administrador", e);
            messages.add(String.format("Error al actualizar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            responseWithData.setMessages(messages);
            return new ResponseEntity<>(responseWithData, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable UUID id) {
        var messages = new ArrayList<String>();
        var response = new GenericResponse();

        try {
            if (id == null) {
                var userMessage = String.format("El ID del %S es requerido para poder eliminar la información.",
                        NAME_CLASS_SINGULAR);
                var technicalMessage = String.format("El ID del %s en la clase AdministratorEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingAdmin = adminService.findById(id);
            if (existingAdmin.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            adminService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el administrador", e);
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private AdministratorEntity updateAdministratorFields(AdministratorEntity existing, AdministratorEntity updates) {
        if (isNotEmpty(updates.getName()))
            existing.setName(updates.getName());
        if (isNotEmpty(updates.getLastName()))
            existing.setLastName(updates.getLastName());
        if (isNotEmpty(updates.getIdType()))
            existing.setIdType(updates.getIdType());
        if (isNotEmpty(updates.getIdNumber()))
            existing.setIdNumber(updates.getIdNumber());
        if (isNotEmpty(updates.getContactNumber()))
            existing.setContactNumber(updates.getContactNumber());
        if (isNotEmpty(updates.getEmail()))
            existing.setEmail(updates.getEmail());
        if (isNotEmpty(updates.getPassword()))
            existing.setPassword(updates.getPassword());
        return existing;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    private ResponseEntity<AdministratorResponse> buildNotFoundResponse(AdministratorResponse response,
            List<String> messages) {
        messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
        response.setMessages(messages);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}