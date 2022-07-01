package com.NoAutenticados.RoyalOak.controllers;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.evento.OnSendBillEvent;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.ClienteProductoPedido;
import com.NoAutenticados.RoyalOak.models.EstadoFactura;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.NoAutenticados.RoyalOak.services.implementacion.ClienteServicioImp;
import com.NoAutenticados.RoyalOak.services.implementacion.GeneradorPdfServiceImp;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class generadorFacturaControlador {
    @Autowired
    private GeneradorPdfServiceImp pdfService;
    @Autowired
    private ClienteServicioImp clienteServicio;
    @Autowired
    private ApplicationEventPublisher eventoPublicador;

    @GetMapping("/clientes/factura")
    public ResponseEntity<?> generadorFacutraPdf(
            HttpServletResponse response,
            Authentication authentication)
            throws DocumentException, IOException, URISyntaxException {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        FacturaDTO factura = cliente.getFacturas().stream().map(factura1 -> new FacturaDTO(factura1)).filter(fact -> fact.getEstadoFactura() == EstadoFactura.CARRITO).findFirst().orElse(null);

        response.setContentType("application/pdf");
        DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy:hh:mm");
        String fechaActual = formatoFecha.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_royalOak-pedido" + fechaActual + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfService.exportar(response, factura);

        return new ResponseEntity<>("Tu factura ha sido creada exitosamente" , HttpStatus.CREATED);
    }

    @GetMapping("/clientes/enviofactura")
    public ResponseEntity<?> envioFactura(Authentication authentication, HttpServletResponse response){
        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        response.setContentType("aplicacion/pdf");
        DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy:hh:mm");
        String fechaActual = formatoFecha.format(new Date());
        eventoPublicador.publishEvent(new OnSendBillEvent(cliente, response));

        return new ResponseEntity<>("Factura enviada exitosamente" , HttpStatus.ACCEPTED);
    }
}
