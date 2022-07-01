package com.NoAutenticados.RoyalOak.services;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.Factura;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface FacturaServicio {
    Set<FacturaDTO> getFacturasDTO();

    Factura getFacturaEnCarrito(Authentication authentication);
    Set<FacturaDTO> getFacturasConfirmadas(Authentication authentication);

    Factura getFacturaById(long id);
    void guardarFactura(Factura factura);
}
