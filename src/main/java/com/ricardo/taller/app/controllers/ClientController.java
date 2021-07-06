package com.ricardo.taller.app.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ricardo.taller.app.dto.ClienteDTO;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.model.Cliente;
import com.ricardo.taller.app.model.KPICliente;
import com.ricardo.taller.app.model.RespuestaControlador;
import com.ricardo.taller.app.service.ClientService;
import com.ricardo.taller.app.util.UtilFunctions;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * RestController de clientes
 * 
 * @author ricardo
 *
 */
@RestController
public class ClientController {

	@Autowired
	private ClientService clientService;


	@ApiOperation("Retorna la lista de todos los clientes")
	@ApiResponses(
		value = {
			@ApiResponse(code = 200, message = "Proceso satisfactorio."),
			@ApiResponse(code = 500, message = "No se pudo obtener clientes.[No se envia data]", response = RespuestaControlador.class)
		}
	)
	@GetMapping("/listclientes")
	public ResponseEntity<RespuestaControlador<List<Cliente>>> listarClientes() {
		RespuestaControlador<List<Cliente>> respuesta = (RespuestaControlador<List<Cliente>>) UtilFunctions.createMessageOk();
		try {
			List<Cliente> clientes = clientService.findAllClients();
			respuesta.setData( clientes );

			return ResponseEntity.ok(respuesta);
		} catch (InterruptedException | ExecutionException e) {
			respuesta = (RespuestaControlador<List<Cliente>>) UtilFunctions.createMessageError("No se pudo obtener clientes: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}

	}


	@ApiOperation("Retorna los KPI de los clientes")
	@ApiResponses(
		value = {
			@ApiResponse(code = 200, message = "Proceso satisfactorio."),
			@ApiResponse(code = 500, message = "No se pudo obtener No se pudo obtener indicadores.[No se envia data]", response = RespuestaControlador.class)
		}
	)
	@GetMapping("/kpideclientes")
	public ResponseEntity<RespuestaControlador<KPICliente>> kpideclientes() {
		RespuestaControlador<KPICliente> respuesta = (RespuestaControlador<KPICliente>) UtilFunctions.createMessageOk();
		try {
			List<Cliente> clientes = clientService.findAllClients();

			BigDecimal average = clientes.stream().map(c -> BigDecimal.valueOf(c.getEdad())).reduce(BigDecimal.ZERO, BigDecimal::add)
					.divide(BigDecimal.valueOf(clientes.size()), 2, BigDecimal.ROUND_HALF_UP);

			
			BigDecimal standardDeviation = BigDecimal.ZERO;
			
			for (Cliente cliente : clientes) {
				standardDeviation = standardDeviation.add(( BigDecimal.valueOf(cliente.getEdad()).subtract(average)).pow(2));
			}
			
			double sq = standardDeviation.doubleValue() / clientes.size();
			double  res = Math.sqrt(sq);
			
			KPICliente kpiCliente = new KPICliente();
			kpiCliente.setPromedio(average);
			kpiCliente.setDesviacionEstandar(res);
			respuesta.setData(kpiCliente);

			return ResponseEntity.ok(respuesta);
		} catch (InterruptedException | ExecutionException e) {
			respuesta = (RespuestaControlador<KPICliente>) UtilFunctions.createMessageError("No se pudo obtener indicadores: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}

	}

	/*
	@GetMapping("/cliente/{id}")
	public ResponseEntity<?> obtenerCliente(@PathVariable String id) {
		Map<String, Object> respuesta = UtilFunctions.createMessageOk();
		Optional<Cliente> optionalCliente;
		try {
			optionalCliente = clientService.findById(id);
			if (optionalCliente.isPresent()) {
				respuesta.put(UtilConstants.CLIENTE, optionalCliente.get());
				return ResponseEntity.ok(respuesta);
			} else {
				respuesta = UtilFunctions.createMessageError("No se encontro al cliente");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
			}
		} catch (InterruptedException | ExecutionException | BusinessException | RequiredException e) {
			respuesta = UtilFunctions.createMessageError("No se pudo obtener cliente: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}

	}
	*/

	@ApiOperation("Crea un nuevo cliente o actualiza uno ya existente si se le manda el id")
	@ApiResponses(
		value = {
			@ApiResponse(code = 200, message = "Proceso satisfactorio."),
			@ApiResponse(code = 500, message = "No se pudo obtener No se pudo obtener indicadores.[No se envia data]", response = RespuestaControlador.class)
		}
	)
	@PostMapping("/creacliente")
	public ResponseEntity<RespuestaControlador<Cliente>> crearCliente(@RequestBody ClienteDTO clienteDTO) {
		RespuestaControlador<Cliente> respuesta = (RespuestaControlador<Cliente>) UtilFunctions.createMessageOk();
		Cliente clienteStorage;
		try {
			clienteStorage = clientService.save(clienteDTO);
			respuesta.setData(clienteStorage);
			return ResponseEntity.ok(respuesta);
		} catch (InterruptedException | ExecutionException | BusinessException | RequiredException e) {
			respuesta = (RespuestaControlador<Cliente>) UtilFunctions.createMessageError("No se registrar al cliente: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}

	}

}
