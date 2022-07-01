package com.NoAutenticados.RoyalOak.services.implementacion;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.EstadoFactura;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.NoAutenticados.RoyalOak.repositories.ClienteRepositorio;
import com.NoAutenticados.RoyalOak.repositories.FacturaRepositorio;
import com.NoAutenticados.RoyalOak.services.FacturaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacturaServicioImp implements FacturaServicio {
    @Autowired
    FacturaRepositorio facturaRepositorio;
    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Override
    public Set<FacturaDTO> getFacturasDTO() {
        return facturaRepositorio.findAll().stream().map(factura -> new FacturaDTO(factura)).collect(Collectors.toSet());
    }

    @Override
    public Factura getFacturaEnCarrito(Authentication authentication) {
        Cliente cliente = clienteRepositorio.findByEmail(authentication.getName());
        return cliente.getFacturas().stream().filter(factura -> factura.getEstadoFactura()== EstadoFactura.CARRITO).findFirst().orElse(null);
    }
    @Override
    public Set<FacturaDTO> getFacturasConfirmadas(Authentication authentication) {
        Cliente cliente = clienteRepositorio.findByEmail(authentication.getName());
        return cliente.getFacturas().stream().filter(factura -> factura.getEstadoFactura()==EstadoFactura.CONFIRMADO).map(factura -> new FacturaDTO(factura)).collect(Collectors.toSet());
    }
    @Override
    public Factura getFacturaById(long id) {
        return facturaRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardarFactura(Factura factura) {
        facturaRepositorio.save(factura);
    }
}
