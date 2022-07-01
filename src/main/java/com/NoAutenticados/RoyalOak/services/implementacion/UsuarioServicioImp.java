package com.NoAutenticados.RoyalOak.services.implementacion;

import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.repositories.ClienteRepositorio;
import com.NoAutenticados.RoyalOak.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Override
    public Cliente getCliente(String token) {
        return clienteRepositorio.findByToken(token);
    }


}
