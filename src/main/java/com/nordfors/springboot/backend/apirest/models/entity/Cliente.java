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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	
	@NotEmpty(message = "no debe estar vacío.")
	@Size(min=3, max=12, message = "debe tener entre 3 y 12 caracteres")
	// @Column se utiliza para darle el nombre a la tabla en la base de datos. Pero si se va a llamar igual no hace falta anotarlo
	// Tambien se puede utilizar para indicar el largo varchar length o nullable, etc
	@Column(nullable=false)
	private String nombre;
	
	@NotEmpty
	@Size(min=2, max=14, message = "debe tener entre 2 y 14 caracteres")
	@Column(nullable=false)
	private String apellido;
	
	@NotEmpty
	@Email
	@Column(nullable=false, unique=true)
	private String email;
	
	@NotNull
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	//@JsonFormat(timezone="America/Argentina/Buenos_Aires")
	private Date createAt;
	
	private String foto;
	
	//Tambien existe @PreUpdate
//	@PrePersist
//	public void prePersist() {
//		createAt = new Date();
//	}
	
	@NotNull(message="la región debe ser seleccionada")
	// Cuando trabajamos con apirest y llamamos al getRegion todos los clientes se transforman en Json automaticamente
	// Por debajo se genera un objeto region que es un proxy/puente para poder acceder a estos datos
	@ManyToOne(fetch=FetchType.LAZY)
	// Si se omite se crea igual tomando el nombre del atributo _nombredellave
	@JoinColumn(name="region_id")
	// Genera atributos automaticamente que con la siguiente anotacion ignoramos, son propios de hibernate del objeto proxy al usar LAZY
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Region region;

	// Cuando se liste un cliente se obitiene la factura pero la factura no va a volver a llamar al cliente. Se evita loop
	@JsonIgnoreProperties(value={"cliente","hibernateLazyInitializer","handler"}, allowSetters=true)
	// Cuando es bidireccional con mappedBy= indicamos el nombre del atributo de la otra clase que hace referencia a esta
	@OneToMany(fetch = FetchType.LAZY, mappedBy="cliente", cascade = CascadeType.ALL)
	private List<Factura> facturas;
	
	
	
	
	
	
	public Cliente() {
		this.facturas = new ArrayList<>();
	}
	
	

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	
	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}





	private static final long serialVersionUID = 1L;

}
