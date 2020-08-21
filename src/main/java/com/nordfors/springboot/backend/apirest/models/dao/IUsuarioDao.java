package com.nordfors.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nordfors.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	// hay que usar las palabras 'find' y con 'by' indicamos la condicion 'where' y el nombre del parametro buscado(atributo username comanzando en mayususcula)
	// A traves del Query method name se ejecuta la consulta JPQL select u from Usuario u where u.username=?
	public Usuario findByUsername(String usuario);
	
	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String usuario);

}
