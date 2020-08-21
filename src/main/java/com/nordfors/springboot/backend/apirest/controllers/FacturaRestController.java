package com.nordfors.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nordfors.springboot.backend.apirest.models.entity.Factura;
import com.nordfors.springboot.backend.apirest.models.entity.Producto;
import com.nordfors.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = {"http://localhosto:4200", "*"}) // Está configrado en el ResourceServerConfig asi que aca se puede omitir
@RestController
@RequestMapping("/api")
public class FacturaRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(code = HttpStatus.OK) // El code = puede omitirse
	public Factura show(@PathVariable Long id) {
		return clienteService.findFacturaById(id);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Ya que es un void y no retorna nada
	public void delete(@PathVariable Long id) {
		clienteService.deleteFactura(id);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/facturas/filtrar-productos/{term}")
	@ResponseStatus(code = HttpStatus.OK)
	//Retorna un json con los productos
	public List<Producto> filtrarProductos(@PathVariable String term){
		return clienteService.findProductoByNombre(term);
	}
	
	// Automaticamente Sprign va a poblar todos los datos que se envian desde el fron en formato json en un objeto factura @RequestBody
	@Secured("ROLE_ADMIN")
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crear(@RequestBody Factura factura) {
		// El metodo saveFactura retorna la factura nueva que se creo en el repository y en la bd
		return clienteService.saveFactura(factura);
	}

}
