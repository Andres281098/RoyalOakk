package com.NoAutenticados.RoyalOak.services;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.ClienteProductoPedido;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public interface GeneradorPdfService {

    void exportar(HttpServletResponse response, FacturaDTO factura) throws IOException, DocumentException, URISyntaxException;
}
