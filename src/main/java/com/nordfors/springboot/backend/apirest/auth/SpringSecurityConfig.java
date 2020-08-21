package com.nordfors.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @EnableGlobalMethodSecurity para habiliatar seguridad con anotaciones
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService usuarioService;
	
	// El metodo se registra(con @Bean) en el contenedor para usarlo en otras configuraciones inyectando con @Autowired
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired // inyecta AuthenticationManagerBuilder
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}
	
	
	//Se crea el metodo y anota con @Bean para reutilizar authorizationManager en la clase AuthorizationServerConfig	
	// Por defecto se registra con el nombre authenticacionManager
	@Bean("authenticationManager") // Esta de mas en este caso especificar un nombre "authenticationManager"
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Se configuran las rutas publicas se haya iniciado no
		http.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		// Se deshabilita la proteccion porque ingresamos desde angular
		.csrf().disable()
		// Se deja sin estado porque es un apirest y se accede desde un cliente, guardando la informacion del usuario en el token y no en la sesion en el lado del servidor
		// STATELESS es un ENUM
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
}
