package com.NoAutenticados.RoyalOak.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String nombre,apellido,email,telefono, contraseña;
    @ElementCollection
    @Column(name="DIRECCION")
    private Set<String> direcciones = new HashSet<>();
    @OneToMany(mappedBy="cliente", fetch=FetchType.EAGER)
    private Set<Factura>facturas = new HashSet<>();
    private RolUsuario rolUsuario;
    private boolean enable;
    private String token;


    public Cliente () {}
    public Cliente(String nombre, String apellido, String email, String telefono, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.rolUsuario = RolUsuario.CLIENTE;
    }

    public long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto(){
        return this.nombre + " " + this.apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getDirecciones() {
        return direcciones;
    }
    public void setDirecciones(Set<String> direcciones) {
        this.direcciones = direcciones;
    }
    public void addDireccion(String direccion) {
        direcciones.add(direccion);
    }
    public Set<Factura> getFacturas() {
        return facturas;
    }
    public void setFacturas(Set<Factura> facturas) {
        this.facturas = facturas;
    }
    public void addFactura(Factura factura) {
        factura.setCliente(this);
        facturas.add(factura);
    }
    public String getContraseña() {
        return contraseña;
    }
    public void setContraseña(String password) {
        this.contraseña = contraseña;
    }
    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
