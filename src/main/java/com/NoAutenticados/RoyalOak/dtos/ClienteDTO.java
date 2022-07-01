package com.NoAutenticados.RoyalOak.dtos;

import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.NoAutenticados.RoyalOak.models.RolUsuario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClienteDTO {

    private long id;

    private String nombre,apellido,email,telefono;
    private Set<String> direcciones;
    private Set<FacturaDTO>facturas = new HashSet<>();
    private RolUsuario rolUsuario;

    public ClienteDTO () {}

    public ClienteDTO (Cliente cliente){
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.email = cliente.getEmail();
        this.telefono = cliente.getTelefono();
        this.direcciones = cliente.getDirecciones();
        this.facturas = cliente.getFacturas().stream().map(factura -> new FacturaDTO(factura)).collect(Collectors.toSet());
        this.rolUsuario = cliente.getRolUsuario();
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
    public Set<String> getDirecciones() {
        return direcciones;
    }
    public void setDirecciones(Set<String> direcciones) {
        this.direcciones = direcciones;
    }
    public Set<FacturaDTO> getFacturas() {
        return facturas;
    }
    public void setFacturas(Set<FacturaDTO> facturas) {
        this.facturas = facturas;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
