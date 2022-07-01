package com.NoAutenticados.RoyalOak.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
//                .antMatchers("/rest/**", "/h2-console/**").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/clientes/**","/api/productos/admin", "/api/facturas", "/api/productos/admin").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/productos").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.PATCH, "/api/clientes/actual/modificar", "/api/clientes/roles", "/api/facturas", "/api/productos").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.DELETE,"/api/productos").hasAuthority("ADMIN")
//
//                .antMatchers(HttpMethod.GET, "/api/clientes/actual","/api/registro/**").hasAuthority("CLIENTE")
//                .antMatchers(HttpMethod.POST, "/api/clientes","/api/clientes/direcciones", "/api/productos/carrito/agregar", "/api/facturas/confirmadas").hasAuthority("CLIENTE")
//                .antMatchers(HttpMethod.PATCH, "/api/productos/carrito/modificar").hasAuthority("CLIENTE")
//                .antMatchers(HttpMethod.DELETE, "/clientes/actual/eliminarDireccion", "/api/productos/carrito/borrar").hasAuthority("CLIENTE")
//
//                .antMatchers(HttpMethod.GET, "/api/registro/**", "/api/productos").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/clientes","/api/clientes/**","/api/productos/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/web/index.html").permitAll()
//                .antMatchers("/web/index.html", "/web/login.html", "/web/registro.html", "/web/pages/contacto.html","/web/pages/random.html").permitAll()
//                .antMatchers("/web/pages/carrito.html").hasAuthority("CLIENT");
                .antMatchers("/api/**").permitAll()
                .antMatchers("/web/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/**").permitAll();



        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
