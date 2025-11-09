package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.UsuarioResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.UsuarioService;
import co.edu.uco.easy.victusresidencias.victus_api.service.TipoIdentificacionService;
import co.edu.uco.easy.victusresidencias.victus_api.service.CiudadService;
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
@RequestMapping("/api/v1/usuarios")
public class UsuarioControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioControlador.class);
    private static final String NAME_CLASS_SINGULAR = "Usuario";
    private static final String NAME_CLASS_PLURAL = "Usuarios";
    private static final String NOT_FOUND_ID_MESSAGE = "No se encontró un %s con el ID especificado.";

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoIdentificacionService tipoIdentificacionService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping("/dummy")
    public UsuarioEntity getDummy() {
        return new UsuarioEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<UsuarioResponse> retrieveAll() {
        var response = new UsuarioResponse();
        var messages = new ArrayList<String>();
        try {
            var entities = usuarioService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error al consultar los usuarios", e);
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAME_CLASS_PLURAL));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> retrieveById(@PathVariable UUID id) {
        var response = new UsuarioResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = usuarioService.findById(id);
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
            LOGGER.error("Error al consultar el usuario", e);
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody UsuarioEntity usuario) {
        var messages = new ArrayList<String>();

        try {
            validateRequiredFields(usuario);
            validateExistingUser(usuario);
            validateRelatedEntities(usuario);

            usuarioService.save(usuario);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAME_CLASS_SINGULAR));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            LOGGER.error("Error de aplicación al crear usuario", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(
                    String.format("Se ha presentado un problema inesperado al registrar el %s.", NAME_CLASS_SINGULAR));
            LOGGER.error("Error inesperado al crear usuario", exception);
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id,
            @RequestBody UsuarioEntity usuario) {
        var response = new UsuarioResponse();
        var messages = new ArrayList<String>();

        try {
            var existingUsuario = usuarioService.findById(id);
            if (existingUsuario.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            var updatedUsuario = updateUsuarioFields(existingUsuario.get(), usuario);
            validateRelatedEntities(updatedUsuario);

            usuarioService.save(updatedUsuario);

            messages.add(String.format("El %s se actualizó correctamente.", NAME_CLASS_SINGULAR));
            response.setData(List.of(updatedUsuario));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al actualizar usuario", e);
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
                var technicalMessage = String.format("El ID del %s en la clase UsuarioEntity llegó nulo.",
                        NAME_CLASS_SINGULAR);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingUsuario = usuarioService.findById(id);
            if (existingUsuario.isEmpty()) {
                messages.add(String.format(NOT_FOUND_ID_MESSAGE, NAME_CLASS_SINGULAR));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            usuarioService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el usuario", e);
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAME_CLASS_SINGULAR));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateRequiredFields(UsuarioEntity usuario) throws BusinessLogicVictusResidenciasException {
        if (usuario.getTipoIdentificacion() == null || usuario.getTipoIdentificacion().getId() == null) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El tipo de identificación es requerido.",
                "El tipo de identificación llegó nulo o con ID nulo."
            );
        }

        if (usuario.getNumeroIdentificacion() == null || usuario.getNumeroIdentificacion().isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El número de identificación es requerido.",
                "El número de identificación llegó nulo o vacío."
            );
        }

        if (usuario.getPrimerNombre() == null || usuario.getPrimerNombre().isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El primer nombre es requerido.",
                "El primer nombre llegó nulo o vacío."
            );
        }

        if (usuario.getPrimerApellido() == null || usuario.getPrimerApellido().isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El primer apellido es requerido.",
                "El primer apellido llegó nulo o vacío."
            );
        }

        if (usuario.getCiudadResidencia() == null || usuario.getCiudadResidencia().getId() == null) {
            throw BusinessLogicVictusResidenciasException.crear(
                "La ciudad de residencia es requerida.",
                "La ciudad de residencia llegó nula o con ID nulo."
            );
        }

        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El correo electrónico es requerido.",
                "El correo electrónico llegó nulo o vacío."
            );
        }

        if (usuario.getNumeroTelefonoMovil() == null || usuario.getNumeroTelefonoMovil().isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El número de teléfono móvil es requerido.",
                "El número de teléfono móvil llegó nulo o vacío."
            );
        }
    }

    private void validateExistingUser(UsuarioEntity usuario) throws BusinessLogicVictusResidenciasException {
        if (usuarioService.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw BusinessLogicVictusResidenciasException.crear(
                "Ya existe un usuario registrado con este correo electrónico.",
                "Se encontró un usuario existente con el correo: " + usuario.getCorreoElectronico()
            );
        }

        if (usuarioService.existsByNumeroIdentificacionAndTipoIdentificacionId(
                usuario.getNumeroIdentificacion(), usuario.getTipoIdentificacion().getId())) {
            throw BusinessLogicVictusResidenciasException.crear(
                "Ya existe un usuario registrado con este número y tipo de identificación.",
                "Se encontró un usuario existente con el número de identificación: " + usuario.getNumeroIdentificacion()
            );
        }
    }

    private void validateRelatedEntities(UsuarioEntity usuario) throws BusinessLogicVictusResidenciasException {
        var tipoIdentificacion = tipoIdentificacionService.findById(usuario.getTipoIdentificacion().getId());
        if (tipoIdentificacion.isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "El tipo de identificación especificado no existe.",
                "No se encontró el tipo de identificación con el ID proporcionado."
            );
        }

        var ciudad = ciudadService.findById(usuario.getCiudadResidencia().getId());
        if (ciudad.isEmpty()) {
            throw BusinessLogicVictusResidenciasException.crear(
                "La ciudad de residencia especificada no existe.",
                "No se encontró la ciudad con el ID proporcionado."
            );
        }

        usuario.setTipoIdentificacion(tipoIdentificacion.get());
        usuario.setCiudadResidencia(ciudad.get());
    }

    private UsuarioEntity updateUsuarioFields(UsuarioEntity existing, UsuarioEntity updates) {
        if (updates.getTipoIdentificacion() != null && updates.getTipoIdentificacion().getId() != null) {
            existing.setTipoIdentificacion(updates.getTipoIdentificacion());
        }
        if (updates.getNumeroIdentificacion() != null && !updates.getNumeroIdentificacion().isEmpty()) {
            existing.setNumeroIdentificacion(updates.getNumeroIdentificacion());
        }
        if (updates.getPrimerNombre() != null && !updates.getPrimerNombre().isEmpty()) {
            existing.setPrimerNombre(updates.getPrimerNombre());
        }
        if (updates.getSegundoNombre() != null) {
            existing.setSegundoNombre(updates.getSegundoNombre());
        }
        if (updates.getPrimerApellido() != null && !updates.getPrimerApellido().isEmpty()) {
            existing.setPrimerApellido(updates.getPrimerApellido());
        }
        if (updates.getSegundoApellido() != null) {
            existing.setSegundoApellido(updates.getSegundoApellido());
        }
        if (updates.getCiudadResidencia() != null && updates.getCiudadResidencia().getId() != null) {
            existing.setCiudadResidencia(updates.getCiudadResidencia());
        }
        if (updates.getCorreoElectronico() != null && !updates.getCorreoElectronico().isEmpty()) {
            existing.setCorreoElectronico(updates.getCorreoElectronico());
        }
        if (updates.getNumeroTelefonoMovil() != null && !updates.getNumeroTelefonoMovil().isEmpty()) {
            existing.setNumeroTelefonoMovil(updates.getNumeroTelefonoMovil());
        }
        existing.setCorreoElectronicoConfirmado(updates.isCorreoElectronicoConfirmado());
        existing.setNumeroTelefonoMovilConfirmado(updates.isNumeroTelefonoMovilConfirmado());
        
        return existing;
    }
}