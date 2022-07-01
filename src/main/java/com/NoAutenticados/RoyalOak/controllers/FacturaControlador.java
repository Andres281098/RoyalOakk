package com.NoAutenticados.RoyalOak.controllers;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.*;
import com.NoAutenticados.RoyalOak.repositories.ClienteProductoPedidoRepositorio;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import com.NoAutenticados.RoyalOak.services.FacturaServicio;
import com.NoAutenticados.RoyalOak.services.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class FacturaControlador {
    @Autowired
    private FacturaServicio facturaServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ClienteProductoPedidoRepositorio clienteProductoPedidoRepositorio;

    @GetMapping("/facturas")
    public Set<FacturaDTO> getFacturasDTO() {
        return facturaServicio.getFacturasDTO();
    }
    @GetMapping("/facturas/confirmadas")
    public Set<FacturaDTO> getFacturasConfirmadas(Authentication authentication) {
        return facturaServicio.getFacturasDTO();
    }

    //-------------------------------------------------Agregar productos al carrito----------------------------------------
    @PostMapping("/productos/carrito/agregar")
    public ResponseEntity<Object> agregarProductosCarrito(Authentication authentication,
                                                          @RequestParam int cantidad,
                                                          @RequestParam long idProducto) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        Factura factura;
        Producto producto;

        if (cliente.getFacturas().stream().filter(fact -> fact.getEstadoFactura() == EstadoFactura.CARRITO).count() == 1) {

            factura = facturaServicio.getFacturaEnCarrito(authentication);
        } else {
            factura = new Factura();
            factura.setEstadoFactura(EstadoFactura.CARRITO);
        }
        if (cantidad <= 0) {
            return new ResponseEntity<>("Faltan datos: Cantidad", HttpStatus.BAD_REQUEST);
        }
        if (productoServicio.findById(idProducto) == null) {
            return new ResponseEntity<>("Producto inexistente", HttpStatus.FORBIDDEN);
        } else {
            producto = productoServicio.findById(idProducto);
        }
        factura.setCliente(cliente);
        factura.setTotalProductos(factura.getTotalProductos() + cantidad);
        facturaServicio.guardarFactura(factura);

        productoServicio.guardarProducto(producto); //duda

        if (factura.getClienteProductoPedidos().stream().filter(pedidito -> pedidito.getProducto() == producto).findAny().orElse(null) == null) {
            ClienteProductoPedido clienteProductoPedido = new ClienteProductoPedido(cantidad, factura, producto);
            clienteProductoPedidoRepositorio.save(clienteProductoPedido);
        } else {
            ClienteProductoPedido pedidoRepetido = factura.getClienteProductoPedidos().stream().filter(pedidito -> pedidito.getProducto() == producto).findAny().orElse(null);
            assert pedidoRepetido != null;
            pedidoRepetido.setCantidad(cantidad);
            pedidoRepetido.setTotal(producto.getPrecio() * pedidoRepetido.getCantidad());
            clienteProductoPedidoRepositorio.save(pedidoRepetido);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //-------------------------------------------FIN Agregar productos al carrito-------------------------------------------
//-----------------------------------------------Modificar carrito------------------------------------------------------
    @PatchMapping("/productos/carrito/modificar")
    public ResponseEntity<Object> modificarProductosCarrito(Authentication authentication,
                                                            @RequestParam int nuevaCantidad,
                                                            @RequestParam long idProducto) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        Factura factura = cliente.getFacturas().stream().filter(fact -> fact.getEstadoFactura() == EstadoFactura.CARRITO).findFirst().orElse(null);

        if (nuevaCantidad <= 0) {
            return new ResponseEntity<>("Faltan datos: Cantidad", HttpStatus.FORBIDDEN);
        }
        assert factura != null;
        if (factura.getProducto().stream().noneMatch(prod -> prod.getId() == idProducto)) {
            return new ResponseEntity<>("El producto no está en el carrito", HttpStatus.FORBIDDEN);
        }

        Producto producto = factura.getProducto().stream().filter(prod -> prod.getId() == idProducto).findFirst().orElse(null);

        ClienteProductoPedido pedidoModificado = Objects.requireNonNull(factura.getClienteProductoPedidos().stream().filter(pedido -> pedido.getProducto() == producto).findFirst().orElse(null));
        factura.setTotalProductos(factura.getTotalProductos() - pedidoModificado.getCantidad() + nuevaCantidad);
        pedidoModificado.setCantidad(nuevaCantidad);
        pedidoModificado.setTotal(pedidoModificado.getCantidad() * producto.getPrecio());
        clienteProductoPedidoRepositorio.save(pedidoModificado);

        productoServicio.guardarProducto(producto);
        facturaServicio.guardarFactura(factura);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//-------------------------------------------Fin Modificar carrito------------------------------------------------------

    //-----------------------------------------------Borrar producto carrito------------------------------------------------------
    @DeleteMapping("/productos/carrito/borrar")
    public ResponseEntity<Object> borrarPedidoCarrito(Authentication authentication,
                                                      @RequestParam long idPedido) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        Factura factura = cliente.getFacturas().stream().filter(fact -> fact.getEstadoFactura() == EstadoFactura.CARRITO).findFirst().orElse(null);

        assert factura != null;
        if (factura.getClienteProductoPedidos().stream().filter(pedido -> pedido.getId() == idPedido).findFirst().orElse(null) == null) {
            return new ResponseEntity<>("El pedido no está en el carrito", HttpStatus.FORBIDDEN);
        }

        ClienteProductoPedido borrarPedido = factura.getClienteProductoPedidos().stream().filter(pedido -> pedido.getId() == idPedido).findFirst().orElse(null);
        clienteProductoPedidoRepositorio.delete(borrarPedido);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

//-----------------------------------------------Fin Borrar producto carrito--------------------------------------------

//-----------------------------------------------Modificar estado factura-----------------------------------------------
    @PatchMapping("/facturas")
    public ResponseEntity<Object> cambiarEstadoFactura(Authentication authentication,
                                                        @RequestParam long idFactura){

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());

        if(facturaServicio.getFacturaById(idFactura) == null)
            return new ResponseEntity<>("La factura no existe.", HttpStatus.FORBIDDEN);

        Factura factura = facturaServicio.getFacturaById(idFactura);

        if(!cliente.getFacturas().contains(factura))
            return new ResponseEntity<>("La factura no corresponde al cliente.", HttpStatus.FORBIDDEN);

        if(!factura.getEstadoFactura().equals(EstadoFactura.CARRITO))
            return new ResponseEntity<>("La factura no está en carrito", HttpStatus.FORBIDDEN);

        factura.setEstadoFactura(EstadoFactura.CONFIRMADO);
        facturaServicio.guardarFactura(factura);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


//-------------------------------------------Fin Modificar estado factura-----------------------------------------------
}