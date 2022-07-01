package com.NoAutenticados.RoyalOak.services.implementacion;

import com.NoAutenticados.RoyalOak.dtos.ClienteDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.repositories.ClienteRepositorio;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClienteServicioImp implements ClienteServicio {

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Override
    public List<ClienteDTO> getClientesDto() {
        return clienteRepositorio.findAll().stream().filter(cliente -> cliente.isEnable()).map(ClienteDTO::new).collect(toList());
    }

    @Override
    public ClienteDTO getCliente(Long id) {
        return clienteRepositorio.findById(id).map(ClienteDTO::new).orElse(null);
    }

    @Override
    public Cliente findByEmail(String email) {
        return clienteRepositorio.findByEmail(email);
    }

    @Override
    public Cliente findByTelefono(String telefono) {
        return clienteRepositorio.findByTelefono(telefono);
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        clienteRepositorio.save(cliente);
    }

    @Override
    public Cliente findByToken(String token) {
        return clienteRepositorio.findByToken(token);
    }


    @Override
    public ClienteDTO getClienteLogueado(String email) {
        return new ClienteDTO(clienteRepositorio.findByEmail(email));
    }

    @Override
    public Cliente findById(long id) {
        return clienteRepositorio.findById(id).orElse(null);
    }

    @Override
    public  Cliente getClientCurrent (Authentication authentication){ return clienteRepositorio.findByEmail(authentication.getName());}

}
