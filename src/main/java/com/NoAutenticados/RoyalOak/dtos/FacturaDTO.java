package com.NoAutenticados.RoyalOak.dtos;

import com.NoAutenticados.RoyalOak.models.EstadoFactura;
import com.NoAutenticados.RoyalOak.models.Factura;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class FacturaDTO {
    private long id;
    private double total = 0;
    private int totalProductos = 0;
    private Set<ClienteProductoPedidoDTO> pedidos = new HashSet<>();
    private EstadoFactura estadoFactura;

    public FacturaDTO() {
    }

    public FacturaDTO(Factura factura) {
        this.id = factura.getId();
        this.pedidos = factura.getClienteProductoPedidos().stream().map(clienteProductoPedido -> new ClienteProductoPedidoDTO(clienteProductoPedido)).collect(Collectors.toSet());
        this.total = factura.getClienteProductoPedidos().stream().mapToDouble(producto -> producto.getTotal()).sum();
        this.estadoFactura = factura.getEstadoFactura();
        this.totalProductos = factura.getTotalProductos();
    }

    public long getId() {
        return id;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public Set<ClienteProductoPedidoDTO> getPedidos() {
        return pedidos;
    }
    public void setPedidos(Set<ClienteProductoPedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }
    public EstadoFactura getEstadoFactura() {
        return estadoFactura;
    }
    public void setEstadoFactura(EstadoFactura estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
    public int getTotalProductos() {
        return totalProductos;
    }
    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }
}
