package com.NoAutenticados.RoyalOak.services;

import com.NoAutenticados.RoyalOak.dtos.ClienteDTO;
import com.NoAutenticados.RoyalOak.models.Cliente;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClienteServicio {

    List<ClienteDTO> getClientesDto();
    ClienteDTO getCliente(@PathVariable Long id);
    ClienteDTO getClienteLogueado(String mail);
    Cliente findById(long id);
    Cliente findByEmail(String email);
    Cliente findByTelefono(String telefono);
    Cliente getClientCurrent (Authentication authentication);
    void guardarCliente(Cliente cliente);

    Cliente findByToken(String token);

}
