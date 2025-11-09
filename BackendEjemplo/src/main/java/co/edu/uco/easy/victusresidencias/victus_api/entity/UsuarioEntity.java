package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class UsuarioEntity extends DomainEntity {

    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private TipoIdentificacionEntity tipoIdentificacion;

    @Column(nullable = false, name = "numero_identificacion", length = 25)
    private String numeroIdentificacion;

    @Column(nullable = false, name = "primer_nombre", length = 20)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 20)
    private String segundoNombre;

    @Column(nullable = false, name = "primer_apellido", length = 20)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 20)
    private String segundoApellido;

    @ManyToOne
    @JoinColumn(name = "ciudad_residencia_id", nullable = false)
    private CiudadEntity ciudadResidencia;

    @Column(nullable = false, name = "correo_electronico", length = 250, unique = true)
    private String correoElectronico;

    @Column(nullable = false, name = "numero_telefono_movil", length = 20)
    private String numeroTelefonoMovil;

    @Column(name = "correo_electronico_confirmado")
    private boolean correoElectronicoConfirmado;

    @Column(name = "numero_telefono_movil_confirmado")
    private boolean numeroTelefonoMovilConfirmado;

    public UsuarioEntity() {
        super(UUIDHelper.getDefault());
        setTipoIdentificacion(null);
        setNumeroIdentificacion(TextHelper.EMPTY);
        setPrimerNombre(TextHelper.EMPTY);
        setSegundoNombre(TextHelper.EMPTY);
        setPrimerApellido(TextHelper.EMPTY);
        setSegundoApellido(TextHelper.EMPTY);
        setCiudadResidencia(null);
        setCorreoElectronico(TextHelper.EMPTY);
        setNumeroTelefonoMovil(TextHelper.EMPTY);
        setCorreoElectronicoConfirmado(false);
        setNumeroTelefonoMovilConfirmado(false);
    }

    public static final UsuarioEntity create() {
        return new UsuarioEntity();
    }

    public TipoIdentificacionEntity getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacionEntity tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = TextHelper.applyTrim(numeroIdentificacion);
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = TextHelper.applyTrim(primerNombre);
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = TextHelper.applyTrim(segundoNombre);
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = TextHelper.applyTrim(primerApellido);
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = TextHelper.applyTrim(segundoApellido);
    }

    public CiudadEntity getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(CiudadEntity ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = TextHelper.applyTrim(correoElectronico);
    }

    public String getNumeroTelefonoMovil() {
        return numeroTelefonoMovil;
    }

    public void setNumeroTelefonoMovil(String numeroTelefonoMovil) {
        this.numeroTelefonoMovil = TextHelper.applyTrim(numeroTelefonoMovil);
    }

    public boolean isCorreoElectronicoConfirmado() {
        return correoElectronicoConfirmado;
    }

    public void setCorreoElectronicoConfirmado(boolean correoElectronicoConfirmado) {
        this.correoElectronicoConfirmado = correoElectronicoConfirmado;
    }

    public boolean isNumeroTelefonoMovilConfirmado() {
        return numeroTelefonoMovilConfirmado;
    }

    public void setNumeroTelefonoMovilConfirmado(boolean numeroTelefonoMovilConfirmado) {
        this.numeroTelefonoMovilConfirmado = numeroTelefonoMovilConfirmado;
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }

    @Override
    public UUID getId() {
        return super.getId();
    }
}