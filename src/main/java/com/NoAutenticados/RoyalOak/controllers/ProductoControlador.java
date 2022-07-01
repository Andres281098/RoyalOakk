package com.NoAutenticados.RoyalOak.controllers;

import com.NoAutenticados.RoyalOak.dtos.ProductoDTO;
import com.NoAutenticados.RoyalOak.models.*;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import com.NoAutenticados.RoyalOak.services.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductoControlador {
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/productos")
    public List<ProductoDTO> getAllActives(){
        return productoServicio.getAllActive();
    }

    @GetMapping("/productos/admin")
    public  List<ProductoDTO> getAll(){
        return productoServicio.getAll();
    }

    @PostMapping("/productos")
    public ResponseEntity<Object> crearProductos(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String imagen,
            @RequestParam double precio,
            @RequestParam int stock,
            @RequestParam Tipo tipo,
            @RequestParam SubTipo subtipo,
            @RequestParam boolean activo,
            Authentication authentication) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        if(cliente.getRolUsuario() != RolUsuario.ADMIN){
            return new ResponseEntity<>("No eres administrador", HttpStatus.FORBIDDEN);
        }
        if(nombre.isEmpty()){
            return new ResponseEntity<>("Faltan datos: Nombre", HttpStatus.FORBIDDEN);
        }
        if(descripcion.isEmpty()){
            return new ResponseEntity<>("Faltan datos: Descripcion", HttpStatus.FORBIDDEN);
        }
        if(imagen.isEmpty()){
            return new ResponseEntity<>("Faltan datos: url imagen", HttpStatus.FORBIDDEN);
        }
        if(stock < 0){
            return new ResponseEntity<>("Stock debe ser mayor o igual a 0", HttpStatus.FORBIDDEN);
        }
        if(precio < 0){
              return new ResponseEntity<>("El precio no puede ser menor o igual a 0", HttpStatus.FORBIDDEN);
        }




        Producto producto = new Producto(nombre, descripcion, imagen,stock, precio,tipo,subtipo);
        producto.setActivo(true);
        productoServicio.guardarProducto(producto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/productos")
    public ResponseEntity<Object> modificarProductos(@RequestParam String nombre,
                                                   @RequestParam String descripcion,
                                                   @RequestParam String imagen,
                                                   @RequestParam double precio,
                                                   @RequestParam int stock,
                                                   @RequestParam Tipo tipo,
                                                   @RequestParam SubTipo subtipo,
                                                   @RequestParam boolean activo,
                                                     @RequestParam long idProducto,
                                                     Authentication authentication) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());

        if(cliente.getRolUsuario() != RolUsuario.ADMIN){
            return new ResponseEntity<>("No eres administrador", HttpStatus.FORBIDDEN);
        }
        if(productoServicio.findById(idProducto)==null)
        {
            return new ResponseEntity<>("El producto no existe.", HttpStatus.FORBIDDEN);
        }

        Producto producto = productoServicio.findById(idProducto);

        if(nombre.isEmpty()){
            return new ResponseEntity<>("Faltan datos: Nombre", HttpStatus.FORBIDDEN);
        }
        if(descripcion.isEmpty()){
            return new ResponseEntity<>("Faltan datos: Descripcion", HttpStatus.FORBIDDEN);
        }
        if(imagen.isEmpty()){
            return new ResponseEntity<>("Faltan datos: url imagen", HttpStatus.FORBIDDEN);
        }
        if(stock < 0){
            return new ResponseEntity<>("Stock debe ser mayor a 0", HttpStatus.FORBIDDEN);
        }
        if(precio < 0){
            return new ResponseEntity<>("El precio no puede ser menor a 0", HttpStatus.FORBIDDEN);
        }
        if(tipo.toString().isEmpty()){
            return new ResponseEntity<>("Faltan datos: tipo de producto", HttpStatus.FORBIDDEN);
        }
        if(subtipo.toString().isEmpty()){
            return new ResponseEntity<>("Faltan datos: subtipo de producto", HttpStatus.FORBIDDEN);
        }

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setImagen(imagen);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setTipo(tipo);
        producto.setSubTipo(subtipo);
        producto.setActivo(activo);
        productoServicio.guardarProducto(producto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/productos")
    public ResponseEntity<Object> borrarProducto(@RequestParam long idProducto, Authentication authentication){

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        if(cliente.getRolUsuario() != RolUsuario.ADMIN){
            return new ResponseEntity<>("No eres administrador", HttpStatus.FORBIDDEN);
        }
        if(productoServicio.findById(idProducto)==null)
        {
            return new ResponseEntity<>("El producto no existe.", HttpStatus.FORBIDDEN);
        }
        Producto producto = productoServicio.findById(idProducto);
        producto.setActivo(false);
        productoServicio.guardarProducto(producto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
