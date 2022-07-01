package com.NoAutenticados.RoyalOak.evento;

import com.NoAutenticados.RoyalOak.models.Cliente;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class OnSendBillEvent extends ApplicationEvent {
    private Cliente cliente;
    private HttpServletResponse response;

    public OnSendBillEvent(Cliente cliente, HttpServletResponse response) {
        super(cliente);
        this.cliente = cliente;
        this.response = response;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
