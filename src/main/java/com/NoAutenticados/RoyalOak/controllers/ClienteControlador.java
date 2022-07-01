package com.NoAutenticados.RoyalOak.controllers;

import com.NoAutenticados.RoyalOak.dtos.ClienteDTO;
import com.NoAutenticados.RoyalOak.evento.OnRegistrationSuccessEvent;
import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.RolUsuario;
import com.NoAutenticados.RoyalOak.repositories.ClienteRepositorio;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import com.NoAutenticados.RoyalOak.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ClienteControlador {

    @Autowired
    ClienteRepositorio clienteRepositorio;
    @Autowired
    ClienteServicio clienteServicio;
    @Autowired
    ApplicationEventPublisher eventoPublicador;
    @Autowired
    public PasswordEncoder passwordEncoder;


    @GetMapping("/clientes") //asigna ruta a un controlador específico
    public List<ClienteDTO> getClientes() {

        //return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
//La función stream permite procesar y transformar (con map) cada elemento de la lista que retorna findAll(), map ejecuta el constructor de la clase ClientDTO y le pasa como parámetro el elemento Client que está procesando en ese momento

//se puede simplificar un poco, como lo único que hace map es ejecutar el método constructor que está en ClientDTO, se puede indicar la referencia a ese método sin tener que declarar toda una expresión lambda:
        return clienteServicio.getClientesDto();

    }

    @GetMapping("/clientes/{id}")
    public ClienteDTO getCliente(@PathVariable Long id) {
        return clienteServicio.getCliente(id);
    }


    @GetMapping("/clientes/actual")
    public ClienteDTO getClient(Authentication authentication) {
        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
        return new ClienteDTO(cliente);
    }

    @PostMapping("/clientes")
    public ResponseEntity<Object> registrarCliente(@RequestParam String nombre,
                                                   @RequestParam String apellido,
                                                   @RequestParam String email,
                                                   @RequestParam String telefono,
                                                   @RequestParam String direccion,
                                                   @RequestParam String contraseña,
                                                   HttpServletRequest request) {
        String pepe;

        if (nombre.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Nombre", HttpStatus.FORBIDDEN);
        }
        if (apellido.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Apellido", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Email", HttpStatus.FORBIDDEN);
        }
        if (telefono.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Telefono", HttpStatus.FORBIDDEN);
        }
        if (direccion.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Dirección", HttpStatus.FORBIDDEN);
        }
        if (contraseña.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Contraseña", HttpStatus.FORBIDDEN);
        }
        if (clienteServicio.findByEmail(email) != null) {
            return new ResponseEntity<>("El email ingresado ya existe", HttpStatus.FORBIDDEN);
        }
        if (clienteServicio.findByTelefono(telefono) != null) {
            return new ResponseEntity<>("El telefono ingresado ya existe", HttpStatus.FORBIDDEN);
        }

        String generarToken = Utils.getToken(65, 90, 8, new Random());
        String urlApp = request.getContextPath();
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, passwordEncoder.encode(contraseña));
        cliente.addDireccion(direccion);
        cliente.setToken(generarToken);
        cliente.setEnable(false);
        clienteRepositorio.save(cliente);
        eventoPublicador.publishEvent(new OnRegistrationSuccessEvent(cliente, urlApp));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clientes/direcciones")
    public ResponseEntity<Object> agregarDirecciones (@RequestParam String direccionNueva,
                                                      Authentication authentication){

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());

        if(direccionNueva.isEmpty())
        {
            return new ResponseEntity<>("Dirección vacía.", HttpStatus.FORBIDDEN);
        }
        if(cliente.getDirecciones().contains(direccionNueva))
        {
            return new ResponseEntity<>("Dirección repetida.", HttpStatus.FORBIDDEN);
        }

        cliente.addDireccion(direccionNueva);
        clienteServicio.guardarCliente(cliente);

        return new ResponseEntity<>("Dirección guardada exitosamente.", HttpStatus.CREATED);
    }

//    @DeleteMapping("/clientes/direcciones")
//    public ResponseEntity<Object> borrarDirecciones (@RequestParam String direccionBorrada,
//                                                      Authentication authentication){
//
//        Cliente cliente = clienteServicio.findByEmail(authentication.getName());
//
//        if(direccionBorrada.isEmpty())
//        {
//            return new ResponseEntity<>("Dirección vacía.", HttpStatus.FORBIDDEN);
//        }
//        if(!cliente.getDirecciones().contains(direccionBorrada))
//        {
//            return new ResponseEntity<>("No se ha encontrado la dirección.", HttpStatus.FORBIDDEN);
//        }
//
//        cliente.getDirecciones().remove(direccionBorrada);
//
//        clienteServicio.guardarCliente(cliente);
//
//        return new ResponseEntity<>("Dirección guardada exitosamente.", HttpStatus.CREATED);
//    }

    @GetMapping("/registro/{token}")
    public ResponseEntity<Object> confirmacionRegistro(HttpServletRequest request,
                                                       @PathVariable String token) {

        Cliente cliente;
        String tokencito;
        if (clienteRepositorio.findByToken(token) == null) {
            return new ResponseEntity<>("Token invalido", HttpStatus.FORBIDDEN);
        } else {

            cliente = clienteRepositorio.findByToken(token);
            tokencito = cliente.getToken();
        }


        if (cliente.isEnable()) {
            return new ResponseEntity<>("El cliente ya está validado", HttpStatus.FORBIDDEN);
        }
        if (tokencito == null) {
            return new ResponseEntity<>("El token ya no es válido", HttpStatus.FORBIDDEN);
        }

        cliente.setEnable(true);
        Utils.borrarToken(tokencito);
        clienteRepositorio.save(cliente);

        return new ResponseEntity<>("Registro de cliente confirmado", HttpStatus.CREATED);
    }
    @PatchMapping("/clientes/roles")
    public ResponseEntity<Object> asignarRoles(@RequestParam long idUsuario,
                                                @RequestParam String mailUsuario,
                                               Authentication authentication){
        if(clienteServicio.findById(idUsuario) == null){
            return new ResponseEntity<>("El usuario no existe.", HttpStatus.FORBIDDEN);
        }

        Cliente usuario = clienteServicio.findById(idUsuario);
        if(!usuario.getEmail().equals(mailUsuario)){
            return new ResponseEntity<>("El email no corresponde al usuario.", HttpStatus.FORBIDDEN);
        }
        usuario.setRolUsuario(RolUsuario.ADMIN);
        clienteServicio.guardarCliente(usuario);

        return new ResponseEntity<>("Usuario con rol de Admin confirmado", HttpStatus.CREATED);
    }

    @GetMapping("/clientes/{token}")
    public Cliente getClientePorToken(@PathVariable String token) {
        return clienteServicio.findByToken(token);
    }

    @PatchMapping("/clientes/actual/modificar")
    public ResponseEntity<Object> modificarCliente(@RequestParam String nombre,
                                                   @RequestParam String apellido,
                                                   @RequestParam String email,
                                                   @RequestParam String telefono,
                                                   @RequestParam Set<String> direccion,
                                                   @RequestParam String contraseña,
                                                   Authentication authentication) {

        Cliente cliente = clienteServicio.getClientCurrent(authentication);

        if (nombre.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Nombre", HttpStatus.FORBIDDEN);
        }
        if (apellido.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Apellido", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Email", HttpStatus.FORBIDDEN);
        }
        if (telefono.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Telefono", HttpStatus.FORBIDDEN);
        }
        if (direccion.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Dirección", HttpStatus.FORBIDDEN);
        }
        if (contraseña.isEmpty()) {
            return new ResponseEntity<>("Faltan datos: Contraseña", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(contraseña, cliente.getContraseña())) {
            return new ResponseEntity<>("La contraseña es incorrecta", HttpStatus.FORBIDDEN);
        }

        cliente.setApellido(apellido);
        cliente.setContraseña(passwordEncoder.encode(contraseña));
        cliente.setDirecciones(direccion);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        clienteServicio.guardarCliente(cliente);
        return new ResponseEntity<>("Cambios Guardados", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/clientes/actual/eliminarDireccion")
    public ResponseEntity<Object> eliminarDireccion(@RequestParam String direccionBorrada,
                                                    @RequestParam String contraseña,
                                                    Authentication authentication) {

        Cliente cliente = clienteServicio.findByEmail(authentication.getName());

        if (direccionBorrada.isEmpty()) {
            return new ResponseEntity<>("Completar Campo Direccion a Borrar", HttpStatus.FORBIDDEN);
        }
        if (contraseña.isEmpty()) {
            return new ResponseEntity<>("Completar Campo Contraseña", HttpStatus.FORBIDDEN);
        }
        if (!cliente.getDirecciones().contains(direccionBorrada)) {
            return new ResponseEntity<>("Direccion no encontrada", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(contraseña, cliente.getContraseña())) {
            return new ResponseEntity<>("La contraseña es incorrecta", HttpStatus.FORBIDDEN);
        }

        cliente.getDirecciones().remove(direccionBorrada);

        clienteServicio.guardarCliente(cliente);
        return new ResponseEntity<>("Direccion Borrada Exitosamente!", HttpStatus.ACCEPTED);
    }
}
