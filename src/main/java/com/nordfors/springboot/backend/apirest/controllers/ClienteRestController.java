package com.nordfors.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nordfors.springboot.backend.apirest.models.entity.Cliente;
import com.nordfors.springboot.backend.apirest.models.entity.Region;
import com.nordfors.springboot.backend.apirest.models.services.IClienteService;
import com.nordfors.springboot.backend.apirest.models.services.IUploadfileService;


//@CrossOrigin(origins= {"http://localhost:4200"}, methods = , allowedHeaders = )  ---   Se pueden limitar los tipos de peticiones y los cabeceros http etc
@CrossOrigin(origins= {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadfileService uploadService;
	
	private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

	// Se usa el de page
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 6);
		return clienteService.findAll(pageable);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.OK) // Es redundante porque por defecto ya tiene el codigo 200 ok
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Cliente cliente = null;
		//Map es la interface y HashMap la implementacion
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e){
			response.put("mensaje", "Error al realizar la consulta a la base de datos.");
			response.put("mensaje", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	// @Valid es necesario usarlo para validar las anotaciones de la clase entity
	// @BindingResult contiene todos los mensajes de error
	@PostMapping("/clientes")
	// @ResponseStatus(HttpStatus.CREATED) // Se le da el codigo 201 created
	// Se usa @RequestBody porque los datos del objeto cliente vienen de angular en formato JSON dentro del cuerpo del request
	// Spring toma esos parametros y puebla o mapea al objeto cliente 
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		//cliente.setCreateAt(new Date());   Ya se asigna en la clase entity con prepersist
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			
		// A partir de JAva 8 se puede hacer lo mismo con menos codigo a traves del API Stream, programacion funcional
//			List<String> errors = new ArrayList<>();
//			
//			for(FieldError err: result.getFieldErrors()) {
//				errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
//			}
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteNew = clienteService.save(cliente);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al registrar al cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con éxito.");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	// Tambien se puede pasar el id desde angular y manerjarlo con clientes/{id}  -- @PathVariable Long id --- findById(id)
	@PutMapping("/clientes")
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result) {
		// return clienteService.save(cliente);    --> Tambien actualiza pero todo. de la siguiente manera solamente losatriubtos json que se envian
		
		Cliente clienteActual = clienteService.findById(cliente.getId());
		
		Cliente clienteUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
				List<String> errors = result.getFieldErrors()
						.stream()
						.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
						.collect(Collectors.toList());
				
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}
		
		if(clienteActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el cliente ID ".concat(cliente.getId().toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());
			clienteActual.setRegion(cliente.getRegion());
			
			
			clienteUpdated = clienteService.save(clienteActual);
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito.");
		response.put("cliente", clienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/clientes/{id}")
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		Cliente cliente = clienteService.findById(id);
		String nombreFotoAnterior = cliente.getFoto();
		
		uploadService.eliminar(nombreFotoAnterior);
		
			// Spring data a traves de CrudRepository va a validar automaticamente que el cliente exista por su id
			clienteService.delete(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente de la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con éxito.");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@Secured("ROLE_ADMIN")
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			
			// Fuera del try/catch se define como null y se define en el try por copiar() que retorna nombre del archivo
			String nombreArchivo = null;
			try {
				nombreArchivo = uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getFoto();
			
			uploadService.eliminar(nombreFotoAnterior);
			
			
			cliente.setFoto(nombreArchivo);
			
			clienteService.save(cliente);
			
			response.put("cliente", cliente);
			response.put("mensaje", "La imagen fue subida correctamente: " + nombreArchivo);
		}
		
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	// .+ expresion regular para indicar que hay una extension
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Resource recurso = null;
		
		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		// Se pasan las cabeceras de las respuestas para forzar a este recurso a que se pueda descargar.. un attachment
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/clientes/regiones")
	public List<Region> listarRegiones(){
		return clienteService.findAllRegiones();
	}
	
}











