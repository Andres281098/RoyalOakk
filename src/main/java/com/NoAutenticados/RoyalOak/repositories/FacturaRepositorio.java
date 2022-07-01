package com.NoAutenticados.RoyalOak.repositories;

import com.NoAutenticados.RoyalOak.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FacturaRepositorio extends JpaRepository <Factura, Long>{
}
