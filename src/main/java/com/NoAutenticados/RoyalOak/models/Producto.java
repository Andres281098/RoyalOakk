package com.NoAutenticados.RoyalOak.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy =  "native")
    private long id;
    private String nombre, descripcion, imagen;
    private Tipo tipo;
    private SubTipo subTipo;
    private int stock;
    private double precio;

    @OneToMany(mappedBy="producto", fetch=FetchType.EAGER)
    private Set<ClienteProductoPedido> clienteProductoPedidos = new HashSet<>();
    private boolean activo;

    public Producto() {}


    public Producto(String nombre, String descripcion, String imagen, int stock, double precio, Tipo tipo, SubTipo subTipo) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.stock = stock;
        this.precio = precio;
        this.activo = false;
        this.tipo = tipo;
        this.subTipo = subTipo;

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
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public Set<ClienteProductoPedido> getClienteProductoPedidos() {
        return clienteProductoPedidos;
    }
    public void setClienteProductoPedidos(Set<ClienteProductoPedido> clienteProductoPedidos) {
        this.clienteProductoPedidos = clienteProductoPedidos;
    }
    public Set<Factura> getFacturas(){
        return clienteProductoPedidos.stream().map(factura -> factura.getFactura()).collect(Collectors.toSet());
    }
    public void addClienteProductoPedido(ClienteProductoPedido clienteProductoPedido) {
        clienteProductoPedido.setProducto(this);
        clienteProductoPedidos.add(clienteProductoPedido);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public SubTipo getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(SubTipo subTipo) {
        this.subTipo = subTipo;
    }
}
