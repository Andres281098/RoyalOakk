package com.NoAutenticados.RoyalOak.repositories;


import com.NoAutenticados.RoyalOak.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClienteRepositorio extends JpaRepository<Cliente,Long> {

    Cliente findByEmail(String email);
    Cliente findByTelefono(String telefono);
    Cliente findByToken(String token);

}
