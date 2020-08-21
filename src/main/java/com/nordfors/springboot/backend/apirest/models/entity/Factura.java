package com.nordfors.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="facturas")
public class Factura implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	
	private String observacion;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	
	// Cuando se liste una factura se obtiene el cliente pero el cliente no va a volver a llamar a la factura. Se evita loop
	@JsonIgnoreProperties(value={"factura","hibernateLazyInitializer","handler"}, allowSetters = true)
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name="cliente_id") no es necesario porque  esta mapeado desde cliente
	private Cliente cliente;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// Es necesario porque no es bidireccional, entonces hay que especificar el nombre de la fk en la tabla de ItemFactura
	@JoinColumn(name="factura_id")
	private List<ItemFactura> items;
	

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	
	public Factura() {
		this.items = new ArrayList<>();
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	
	// Spring convierte los objetos a json usando el api Jackson, todo por debajo, pero estas conversiones las realiza mediante los métodos getters del objeto, para poder acceder a sus valores y pasarlos al json, por eso es necesario y la importancia de los métodos get.
	public Double getTotal() {
		Double total = 0.00;
		for(ItemFactura item: items) {
			total += item.getImporte();
		}
		return total;
	}




	private static final long serialVersionUID = 1L;
}
