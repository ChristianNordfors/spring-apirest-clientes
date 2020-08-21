package com.nordfors.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

// Por el lado de SPRING
@Configuration
// Hay que habilitar el AuthorizationServer
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	// Se especifica el nombre pero solamente hay uno en este caso
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;

	// Se configuran los permisos de nuestros endpoints. Hay dos endpoints y la ruta que se encarga de obtener las credenciales y generar el token debe ser completamente publica
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// DOS ENDPOINTS: protegidas via http basic - client id + client secret. Se envian en las cabeceras de la peticion
		// para poder autenticarse en el endpoint de login: "/oauth/token/"
		security.tokenKeyAccess("permitAll()")
		// para poder checkear o validar el token y sufirma - "/oauth/check_token"
		.checkTokenAccess("isAuthenticated()");
	}

	//Doble autenticacion, por el usuario y por el cliente. Puede haber varias aplicaciones conectadas al mismo apirest
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
		// Define el alcance que va a tener el cliente(aplicacion con angular), lectura, escritura, modificar, eliminar etc
		.scopes("read", "write")
		// Tipo de autorizacion, es decir, como se va a obtener el token, en este caso password. Con credeciales, cuando los usuarios existen en la base de datos
		.authorizedGrantTypes("password",  "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600); // Se obtiene el nuevo token antes de que caduque para seguir logueado
		// and(). para mas configuracion de otros clientes 
	}

	// Se encarga del proceso de autenticacion y de validar el token. Cada vez que iniciamos sesion(con ususario y contraseña) genera un token
	// para el usuario y acceder a los distintos recursos y paginas pero para eso se tiene que validar y eso tambien se realiza en endpoints, unas rutas que maneja el servidor de autenticacion
	// tanto el login donde genera el token como el proceso de validacion con sus firmas
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		// Aca se registra la informacion adicional (clase InfoAdicionalToken)
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		
		endpoints.authenticationManager(authenticationManager)
		// Esto es opcional, aca se esta haciendo de forma explicita pero se hace automaticamente en EndPointsConfigurer
		.tokenStore(tokenStore())
		// Componente(accessTokenConverter) encargado de almacenar los datos por ej nombre, apellido, NO informacion sensible como tarjeta de credito o pass. Traduce estos valores para la autenticacion y verificar que sus firmas sean las correctas 
		// Por debajo lo va a utilizar el JwtTokenStorage, otro componente que se encarga de crear el jwt, de eliminar, buscar un token asociado a un cliente, todo lo que sea asociado a la persistencia
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
	}
	
	// Esto es opcional, aca se esta haciendo de forma explicita pero se hace automaticamente en EndPointsConfigurer
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		// Si no se especifica esto se hace automaticamente-- Llave tipo MAC para la firma del token
		jwtAccessTokenConverter.setSigningKey(JwtConfig.LLAVE_SECRETA);
		return jwtAccessTokenConverter;
	}
	
	
	

}
