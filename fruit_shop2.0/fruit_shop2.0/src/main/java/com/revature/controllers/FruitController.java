package com.revature.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.revature.dtos.FruitDTO;
import com.revature.exceptions.FruitNotFoundException;
import com.revature.models.Fruit;
import com.revature.service.FruitService;

@RestController
@RequestMapping("/fruit")

public class FruitController {

	private FruitService fs;
	private static final Logger LOG = LoggerFactory.getLogger(FruitController.class);

	@Autowired
	public FruitController(FruitService fs) {
		super();
		this.fs = fs; 
	}  
	
	@GetMapping
	public ResponseEntity<List<FruitDTO>> getAll(@RequestParam(name = "name", required = false) String name) throws FruitNotFoundException{
		MDC.put("requestId", UUID.randomUUID().toString());
		if(name!= null) {
			List<FruitDTO> fruits = new ArrayList<>();
			fruits.add(fs.getFruitByName(name));
			return new ResponseEntity<>(fruits, HttpStatus.OK); 
		}
		LOG.info("All fruits were found. ");
		return new ResponseEntity<>(fs.getAll(), HttpStatus.OK);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<FruitDTO>> getById(@PathVariable int id) throws FruitNotFoundException{
		MDC.put("Fruit has been found by id: ", id);
		List<FruitDTO> fruits = new ArrayList<>();
		fruits.add(fs.getFruitById(id));
		return new ResponseEntity<>(fruits, HttpStatus.OK); 
	}
	
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody Fruit fruit){
		MDC.put("Fruit has been created: ", fruit.getName());
		Fruit f = fs.createFruit(fruit);
		LOG.info("Fruit " + f.getName() + " was created. ");
		return new ResponseEntity<>("Fruit " + f.getName() + " has been created. ", HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Fruit> updateFruit(@RequestBody Fruit fruit, @PathVariable("id") int id) throws FruitNotFoundException{
		MDC.put("Fruit has been created: ", fruit.getName());
		LOG.info("Fruit " + fruit.getName() + " was updated. ");
		return new ResponseEntity<>(fs.updateFruit(id, fruit), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") int id) throws FruitNotFoundException{
		fs.deleteFruit(id);
		LOG.info("Fruit by id: " + id + " was deleted. ");
		MDC.put("Fruit has been deleted by id: ", id);
		return new ResponseEntity<>("User by id:  " + id + " was deleted.", HttpStatus.OK);
	}
}
