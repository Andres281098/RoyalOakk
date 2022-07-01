package com.NoAutenticados.RoyalOak.configurations;

import com.NoAutenticados.RoyalOak.models.Cliente;
import com.NoAutenticados.RoyalOak.models.RolUsuario;
import com.NoAutenticados.RoyalOak.repositories.ClienteRepositorio;
import com.NoAutenticados.RoyalOak.services.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClienteRepositorio clienteRepositorio;
    @Autowired
    ClienteServicio clienteServicio;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(clienteEmail-> {

            Cliente cliente = clienteRepositorio.findByEmail(clienteEmail);

            if (cliente != null) {
                if(cliente.isEnable()) {
                    if (cliente.getRolUsuario()== RolUsuario.ADMIN) {
                        return new User(cliente.getEmail(), cliente.getContraseña(), AuthorityUtils.createAuthorityList("ADMIN"));
                    } else {
//                        cliente.setRolUsuario(RolUsuario.CLIENTE);
//                        clienteServicio.guardarCliente(cliente);
                        return new User(cliente.getEmail(), cliente.getContraseña(), AuthorityUtils.createAuthorityList("CLIENTE"));
                    }
                } else {
                    throw new UsernameNotFoundException("Usuario aun no verificado: " + clienteEmail);
                }
            } else {
                throw new UsernameNotFoundException("Usuario no registrado: " + clienteEmail);
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
