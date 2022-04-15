package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.revature.controllers.UserController;
import com.revature.dtos.FruitDTO;
import com.revature.exceptions.FruitNotFoundException;
import com.revature.models.Fruit;
import com.revature.repositories.FruitRepository;

@Service
public class FruitService {

	@Autowired
	private FruitRepository fr;
	public FruitService (FruitRepository fr) {
		super();
		this.fr = fr; 
	}
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	public List<FruitDTO> getAll(){
		List<Fruit> fruits = fr.findAll();
		List<FruitDTO> protectFruits = new ArrayList<>(); 
		for(Fruit f : fruits) {
				protectFruits.add(new FruitDTO(f));
			}
		return protectFruits; 
	}
	public FruitDTO getFruitById(int id) throws FruitNotFoundException{
		if(id < 1) {
			LOG.error("No fruits exist ");
			throw new FruitNotFoundException();
		}
		LOG.info("Fruit by id: " + id + " was found. ");
		Fruit f1 = fr.getById(id);
		return new FruitDTO(f1);
	}
	
	public FruitDTO getFruitByName(String name) throws FruitNotFoundException{
		Fruit f1 = fr.findFruitByName(name); 
		return new FruitDTO(f1); 
	}
	
	@Transactional
	public Fruit createFruit(Fruit newFruit) {
		return fr.save(newFruit); 
	}
	
	@Transactional
	public Fruit updateFruit(int id, Fruit fruit) throws FruitNotFoundException{
		return fr.save(fruit); 
	}
	 
	@Transactional 
	public boolean deleteFruit(int id) throws FruitNotFoundException{
		getFruitById(id); 
		fr.deleteById(id);
		return true; 
	}
}
