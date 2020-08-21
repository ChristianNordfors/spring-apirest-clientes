package com.nordfors.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nordfors.springboot.backend.apirest.models.entity.Cliente;
import com.nordfors.springboot.backend.apirest.models.entity.Region;

public interface IClienteDao extends JpaRepository<Cliente, Long>{
	
	// Este método podría estar en una interface IRegionDao con su propio Crud o JpaRepository
	// En el Query se trabaja con objetos y no con tablas. Region es el nombre de la clase
	@Query("from Region")
	public List<Region> findAllRegiones();

}
