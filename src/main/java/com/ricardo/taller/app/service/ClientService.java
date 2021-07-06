package com.ricardo.taller.app.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.ricardo.taller.app.dto.ClienteDTO;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.model.Cliente;


public interface ClientService {

	public Cliente save(ClienteDTO clienteDTO)  throws InterruptedException, ExecutionException, BusinessException, RequiredException;
	public Optional<Cliente> findById(String id) throws InterruptedException, ExecutionException, BusinessException, RequiredException;
	public Optional<Cliente> findByName(String name, String lastname) throws InterruptedException, ExecutionException, BusinessException, RequiredException;
	public List<Cliente> findAllClients() throws InterruptedException, ExecutionException;
	
}
