package com.NoAutenticados.RoyalOak.evento;

import com.NoAutenticados.RoyalOak.dtos.FacturaDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.EstadoFactura;
import com.NoAutenticados.RoyalOak.models.Factura;
import com.NoAutenticados.RoyalOak.services.GeneradorPdfService;
import com.NoAutenticados.RoyalOak.services.implementacion.GeneradorPdfServiceImp;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

@Component
public class SendBillListener implements ApplicationListener<OnSendBillEvent> {
    @Autowired
    private GeneradorPdfServiceImp pdfService;
    @Autowired
    private MessageSource mensaje;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnSendBillEvent event) {
        try {
            this.sendBill(event);
        } catch (MessagingException | DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendBill(OnSendBillEvent event) throws MessagingException, DocumentException, IOException, URISyntaxException {
        Cliente cliente = event.getCliente();
        FacturaDTO factura = cliente.getFacturas().stream().map(factura1 -> new FacturaDTO(factura1)).filter(fact -> fact.getEstadoFactura() == EstadoFactura.CARRITO).findFirst().orElse(null);
        HttpServletResponse response = event.getResponse();

        pdfService.exportar(response, factura);

        String emailCliente = cliente.getEmail();
        String emailEmisor = "noreply.royaloak@gmail.com";
        String nombreEmisor = "Royal Oak Staff";
        String asuntoEmail = "Envío de Factura pedido";
        String contenidoEmail = "<h2 style=\"color: black;\">\n Gracias <spa style=\"color: yellow;\"n>Cliente</spa>  por tu compra!\n </h2>\n"
                +"<p style=\"color: #333533;\">\n Gracias por confiar en nosotros y nuestros productos!\n </p>\n"
                +nombreEmisor;
        contenidoEmail = contenidoEmail.replace("Cliente", cliente.getNombreCompleto());
        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper mensajeAyudador = new MimeMessageHelper(mensaje);
        mensajeAyudador.setFrom(emailEmisor);
        mensajeAyudador.setTo(emailCliente);
        mensajeAyudador.setSubject(asuntoEmail);
        mensajeAyudador.setText(contenidoEmail,true);
        javaMailSender.send(mensaje);
    }
}
