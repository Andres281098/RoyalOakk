package com.NoAutenticados.RoyalOak.services.implementacion;

import com.NoAutenticados.RoyalOak.dtos.ProductoDTO;
import com.NoAutenticados.RoyalOak.models.Producto;
import com.NoAutenticados.RoyalOak.repositories.ProductoRepositorio;
import com.NoAutenticados.RoyalOak.services.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServicioImp implements ProductoServicio {
    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public List<ProductoDTO> getAllActive(){
        return productoRepositorio.findAll().stream().filter(producto -> producto.isActivo()).map(producto -> new ProductoDTO(producto)).collect(Collectors.toList());
    }


    @Override
    public void guardarProducto(Producto producto) {
        productoRepositorio.save(producto);
    }

    @Override
    public Producto findById(long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    @Override
    public List<ProductoDTO> getAll() {
        return productoRepositorio.findAll().stream().map(producto -> new ProductoDTO(producto)).collect(Collectors.toList());
    }
}
