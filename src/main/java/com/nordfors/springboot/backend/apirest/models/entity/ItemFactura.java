package com.nordfors.springboot.backend.apirest.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="facturas_items")
public class ItemFactura implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	// ItemFactura es el dueño de la relacion con Producto por lo tanto de forma automatica va a generar la fk basado en el nombre del atributo + _id
	//@JoinColumn(name="producto_id")   <--- asi que esto no es necesario a menos que se quiera cambiar el nombre del fk
	private Producto producto;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	// Spring convierte los objetos a json usando el api Jackson, todo por debajo, pero estas conversiones las realiza mediante los métodos getters del objeto, para poder acceder a sus valores y pasarlos al json, por eso es necesario y la importancia de los métodos get.
	// Calcula el importe de la linea. Se llama getImporte para que lo incluya despues el json ?
	public Double getImporte() {
		return cantidad.doubleValue()*producto.getPrecio();
	}

	private static final long serialVersionUID = 1L;

}
