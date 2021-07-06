package com.ricardo.taller.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.taller.app.dto.ClienteDTO;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.model.Cliente;
import com.ricardo.taller.app.repository.ClientRepository;
import com.ricardo.taller.app.service.ClientService;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Implementacion del servicio de clientes con BD transaccional
 * 
 * @author ricardo
 *
 */
@Service
@Transactional
@Primary
public class ClientServiceBDImpl implements ClientService {
	
	@Autowired
	private ClientRepository clienteRepository;

	@Override
	public Cliente save(ClienteDTO clienteDTO)
			throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		Cliente cliente = convertDTOtoClient(clienteDTO);
		validarRegistroClienteBasico(cliente);
		cliente = clienteRepository.save(cliente);
		return cliente;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Cliente> findById(String id)
			throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		return clienteRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Cliente> findByName(String name, String lastname)
			throws InterruptedException, ExecutionException, BusinessException, RequiredException {
		return clienteRepository.findByNombreIgnoreCaseAndApellidoIgnoreCase(name, lastname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAllClients() throws InterruptedException, ExecutionException {
		return clienteRepository.findAll();
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
	
	private Cliente convertDTOtoClient(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setId(dto.getId());
		cliente.setNombre(dto.getNombre());
		cliente.setApellido(dto.getApellido());
		cliente.setFechaNacimiento(dto.getFechaNacimiento());
		return cliente;
	}


}
