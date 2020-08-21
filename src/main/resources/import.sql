
INSERT INTO regiones (id, nombre) VALUES (1, 'Sudamérica');
INSERT INTO regiones (id, nombre) VALUES (2, 'Centroamérica');
INSERT INTO regiones (id, nombre) VALUES (3, 'Norteamérica');
INSERT INTO regiones (id, nombre) VALUES (4, 'Europa');
INSERT INTO regiones (id, nombre) VALUES (5, 'Asia');
INSERT INTO regiones (id, nombre) VALUES (6, 'África');
INSERT INTO regiones (id, nombre) VALUES (7, 'Oceanía');
INSERT INTO regiones (id, nombre) VALUES (8, 'Antártida');


INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(1, 'Christian', 'Nordfors', 'cnordfors@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(2, 'Christian', 'Nordfors', 'cnordfors1@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Christian', 'Nordfors', 'cnordfors2@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Christian', 'Nordfors', 'cnordfors3@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5, 'Christian', 'Nordfors', 'cnordfors4@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6, 'Christian', 'Nordfors', 'cnordfors5@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7, 'Christian', 'Nordfors', 'cnordfors6@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(8, 'Christian', 'Nordfors', 'cnordfors7@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5,'Christian', 'Nordfors', 'cnordfors8@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6,'Christian', 'Nordfors', 'cnordfors9@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Christian', 'Nordfors', 'cnordfors10@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4,'Christian', 'Nordfors', 'cnordfors11@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7,'Christian', 'Nordfors', 'cnordfors12@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(1, 'Christian', 'Nordfors', 'cnordfors13@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(2, 'Christian', 'Nordfors', 'cnordfors14@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Christian', 'Nordfors', 'cnordfors15@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Christian', 'Nordfors', 'cnordfors16@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5, 'Christian', 'Nordfors', 'cnordfors17@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6, 'Christian', 'Nordfors', 'cnordfors18@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7, 'Christian', 'Nordfors', 'cnordfors19@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(8, 'Christian', 'Nordfors', 'cnordfors20@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5,'Christian', 'Nordfors', 'cnordfors21@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6,'Christian', 'Nordfors', 'cnordfors22@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Christian', 'Nordfors', 'cnordfors23@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4,'Christian', 'Nordfors', 'cnordfors24@gmail.com', '20-08-06');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7,'Christian', 'Nordfors', 'cnordfors25@gmail.com', '20-08-06');


/* Usuarios con roles */
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('christian','$2a$10$2/qw5fWQEm9S9gcIL6BDjuH0HBBG2JXxE3/CvCccA2mxaS10eynoW',1, 'Christian', 'Nordfors', 'christian123@mail.com');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('admin','$2a$10$Xo.2oMNiBYn2UZ5svL3DBuUr9CoYelTy8JjibIcE2k4dNbi/SnsZy',1, 'John', 'Doe', 'jdoe@mail.com');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 1);



/* Populate tabla productos */
INSERT INTO productos (nombre, precio, create_at) VALUES('Panasonic Pantalla LCD', 259990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Camara digital DSC-W320B', 123490, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Apple iPod shuffle', 1499990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Notebook Z110', 37990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Hewlett Packard Multifuncional F2280', 69990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Bianchi Bicicleta Aro 26', 69990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Mica Comoda 5 Cajones', 299990, NOW());



/* facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura equipos de oficina', null, 1, NOW());

INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 6);

