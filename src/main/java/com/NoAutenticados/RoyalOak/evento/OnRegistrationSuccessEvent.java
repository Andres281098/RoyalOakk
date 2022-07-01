package com.NoAutenticados.RoyalOak.evento;

import com.NoAutenticados.RoyalOak.models.Cliente;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationSuccessEvent extends ApplicationEvent {

    private String urlApp;
    private Cliente cliente;

    public OnRegistrationSuccessEvent(Cliente cliente,String urlApp){
        super(cliente);
        this.cliente = cliente;
        this.urlApp = urlApp;
    }

    public String getUrlApp() {
        return urlApp;
    }

    public void setUrlApp(String urlApp) {
        this.urlApp = urlApp;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
