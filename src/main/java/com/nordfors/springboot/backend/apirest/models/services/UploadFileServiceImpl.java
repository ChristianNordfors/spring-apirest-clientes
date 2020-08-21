package com.nordfors.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadfileService {

	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

	private final static String DIRECTORIO_UPLOAD = "uploads";

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {

		Path rutaArchivo = getPath(nombreFoto);

		log.info("Ruta del archivo descarga:" + rutaArchivo.toString());

		Resource recurso = new UrlResource(rutaArchivo.toUri());

		if (!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();

			recurso = new UrlResource(rutaArchivo.toUri());

			log.error("Error no se pudo cargar la imagen: " + nombreFoto);
			// throw new RuntimeException("Error no se pudo cargar la imagen");
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		
		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		// Como está dentro del proyecto se define como  una ruta relativa, si estuviese en el disco c sería absoluta.. C://.. etc
		//Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
		Path rutaArchivo = getPath(nombreArchivo);
		
		log.info("Ruta del archivo subida:" + rutaArchivo.toString());
		
	
			// copy() agrega el archivo a la ruta
			Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		if(nombreFoto != null && nombreFoto.length()>0) {
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
