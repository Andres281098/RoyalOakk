package com.NoAutenticados.RoyalOak.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    private double total;
    private int totalProductos;
    private EstadoFactura estadoFactura;

    @OneToMany(mappedBy="factura", fetch=FetchType.EAGER)
    private Set<ClienteProductoPedido> clienteProductoPedidos = new HashSet<>();


    public Factura() {}

    public Factura(Cliente cliente, double total) {
        this.cliente = cliente;
        this.total = total;
        this.estadoFactura = EstadoFactura.CARRITO;
    }

    public long getId() {
        return id;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public int getTotalProductos() {
        return totalProductos;
    }
    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Set<ClienteProductoPedido> getClienteProductoPedidos() {
        return clienteProductoPedidos;
    }
    public Set<Producto> getProducto(){
        return clienteProductoPedidos.stream().map(producto -> producto.getProducto()).collect(Collectors.toSet());
    }
     public void addClienteProductoPedido(ClienteProductoPedido clienteProductoPedido) {
        clienteProductoPedido.setFactura(this);
        clienteProductoPedidos.add(clienteProductoPedido);
    }
    public EstadoFactura getEstadoFactura() {
        return estadoFactura;
    }
    public void setEstadoFactura(EstadoFactura estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
}
