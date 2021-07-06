package com.ricardo.taller.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.ricardo.taller.app.dto.ClienteDTO;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.model.Cliente;
import com.ricardo.taller.app.service.ClientService;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Implementacion del servicio de clientes integrado con firebase
 * 
 * @author ricardo
 *
 */
@Service
public class ClientServiceImpl  implements ClientService {
	
	private Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
	
	public static final  String COLLECTION_NAME_CLIENTS = "clients";

	@Override
	public List<Cliente> findAllClients() throws InterruptedException, ExecutionException {
		List<Cliente> clientes = new ArrayList<>();
		Firestore db = FirestoreClient.getFirestore();
		CollectionReference colRef = db.collection(COLLECTION_NAME_CLIENTS);
		// Retornar el documento asincrono
		ApiFuture<QuerySnapshot> future = colRef.get();
		// Response
		QuerySnapshot collection = future.get();
		if (collection != null && collection.size() > 0) {
			collection.forEach(t -> {
				Cliente cliente = t.toObject(Cliente.class);
				cliente.setId(t.getId());
				clientes.add(cliente);
			});
		}
	    return clientes;
	}
	
	@Override
	public Optional<Cliente> findByName(String name, String lastname)
			throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> future =
			    db.collection(COLLECTION_NAME_CLIENTS)
			    .get();
		// future.get() blocks on response
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		Optional<Cliente> clienteOptional = Optional.empty();
		if (documents.size() > 0) {
			
			for (QueryDocumentSnapshot document : documents) {
				Cliente cliente = document.toObject(Cliente.class);
				if (cliente.getApellido().equalsIgnoreCase(lastname) && cliente.getNombre().equalsIgnoreCase(name)) {
					cliente.setId(document.getId());
					clienteOptional = Optional.of(cliente);
					break;
				}
			}
		}
		return clienteOptional;
	}
	
	@Override
	public Optional<Cliente> findById(String id)
			throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(COLLECTION_NAME_CLIENTS).document(id);
		// asynchronously retrieve the document
		ApiFuture<DocumentSnapshot> future = docRef.get();
		// block on response
		DocumentSnapshot document = future.get();
		Cliente client = null;
		Optional<Cliente> optionalCliente = Optional.empty();
		if (document.exists()) {
		  // convert document to POJO
		  client = document.toObject(Cliente.class);
		  client.setId(document.getId());
		  return Optional.of(client);
		} else {
		  logger.error("No se encontro documento cliente con ese id");
		  return optionalCliente;
		}
	}

	@Override
	public Cliente save(ClienteDTO clienteDTO) throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		Cliente cliente = ConvertDTOtoClient(clienteDTO);
		validarRegistroClienteBasico(cliente);
		Firestore db = FirestoreClient.getFirestore();
		if (StringUtils.isEmpty(UtilFunctions.verifyString(cliente.getId()))) {
			ApiFuture<DocumentReference> addedDocRef = db.collection(COLLECTION_NAME_CLIENTS).add(cliente);
			String id = addedDocRef.get().getId();
			logger.info("Cliente creado con ID: ".concat(id));
			cliente.setId(id);
		} else {
			Optional<Cliente> optClienteOptional = findById(cliente.getId());
			if (optClienteOptional.isPresent()) {
				Map<String, Object> updateParams = new HashMap<>();
				updateParams.put("nombre", cliente.getNombre());
				updateParams.put("apellido", cliente.getApellido());
				updateParams.put("fechaNacimiento", cliente.getFechaNacimiento());
				db.collection(COLLECTION_NAME_CLIENTS).document(cliente.getId()).update(updateParams);
			} else {
				throw new BusinessException("No se encontro al cliente con el id");
			}
		}
		
		return cliente;
	}
	
	private void validarRegistroClienteBasico(Cliente cliente) throws BusinessException, RequiredException, InterruptedException, ExecutionException {
		if (cliente == null) {
			throw new BusinessException("No ha ingresado el cliente");
		}
		if (StringUtils.isEmpty(cliente.getNombre())) {
			throw new RequiredException("El nombre no puede ser vacio");
		}
		if (StringUtils.isEmpty(cliente.getApellido())) {
			throw new RequiredException("El apellido no puede ser vacio");
		}
		if (StringUtils.isEmpty(UtilFunctions.verifyString(cliente.getFechaNacimiento()))) {
			throw new RequiredException("La fecha de nacimiento no puede estar vacia");
		}
		if (cliente.getFechaNacimiento().after(new Date())) {
			throw new RequiredException("La fecha de nacimiento no puede superar la fecha de hoy");
		}
		Optional<Cliente> optCliente = findByName(cliente.getNombre(), cliente.getApellido());
		
		if (optCliente.isPresent() && !optCliente.get().getId().equals(cliente.getId())) {
			throw new BusinessException("Ya existe un cliente con el mismo nombre y apellido");
		}
	}
	
	private Cliente ConvertDTOtoClient(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setId(dto.getId());
		cliente.setNombre(dto.getNombre());
		cliente.setApellido(dto.getApellido());
		cliente.setFechaNacimiento(dto.getFechaNacimiento());
		return cliente;
	}

	



}
