package com.ricardo.taller.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.taller.app.model.Cliente;

public interface ClientRepository extends JpaRepository<Cliente, String> {

	public Optional<Cliente> findByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);
	
}
