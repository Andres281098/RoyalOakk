package com.NoAutenticados.RoyalOak.services.implementacion;

import com.NoAutenticados.RoyalOak.dtos.ClienteProductoPedidoDTO;
import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.ClienteProductoPedido;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import com.NoAutenticados.RoyalOak.services.GeneradorPdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GeneradorPdfServiceImp implements GeneradorPdfService {
    @Autowired
    ClienteServicio clienteServicio;
    public void exportar(HttpServletResponse response, FacturaDTO factura) throws IOException, DocumentException, URISyntaxException {
        Set<ClienteProductoPedidoDTO> pedidos = factura.getPedidos();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Imagen Logo
        //Path path = Paths.get(ClassLoader.getSystemResource("logo-royaloak.png").toURI());
        Image img = Image.getInstance("https://res.cloudinary.com/royal-oak-imagenes/image/upload/v1656443331/logo-ro_xmp8hl.png");
        img.setAbsolutePosition(35f,745f);
        img.scaleAbsolute(50f,60f);
        //img.setAlignment(Image.ALIGN_RIGHT);

        //Titulo de Factura - Tipo de letra
        Font letraTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        letraTitulo.setSize(18);
        Paragraph parrafo = new Paragraph("Comprobante de Compra", letraTitulo);
        parrafo.setAlignment(Paragraph.ALIGN_CENTER);

        //Contenido Factura - Tipo de letra
        Font letraContenido = FontFactory.getFont(FontFactory.HELVETICA);
        letraContenido.setSize(14);
        /* Valor automatico-dinamico */
        Paragraph parrafo2 = new Paragraph("N° de Comprobante: "+ factura.getId() , letraContenido);
        parrafo2.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph parrafo3 = new Paragraph("Royal Oak - Delivery Bar", letraContenido);
        parrafo3.setAlignment(Paragraph.ALIGN_CENTER);

        // Tabla con Lista de los productos comrpados

        document.addCreationDate();
        document.addAuthor("Royal Oak");
        document.addTitle("Lista de Productos");
        document.addCreator("Royal Oak - Contabilidad");

        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{2, 1, 1,1});
        //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell("Nombre Producto");
        table.addCell("Cantidad");
        table.addCell("Precio Unitario");
        table.addCell("Total Producto");

        for (ClienteProductoPedidoDTO item:
                pedidos) {
            table.addCell(item.getProducto()+ " - " + item.getSubTipo().toString().toLowerCase());
            table.addCell(String.valueOf(item.getCantidad()));
            table.addCell(String.valueOf(item.getPrecio()));
            table.addCell(String.valueOf("$"+item.getTotal()));
        }

        table.addCell("Total");
        table.addCell("");
        table.addCell("");
        table.addCell(String.valueOf(factura.getTotal()));

        document.add(img);
        document.add(parrafo);
        document.add(parrafo2);
        document.add(parrafo3);
        document.add(new Paragraph(" "));
        document.add(table);

        document.close();
    }
}
