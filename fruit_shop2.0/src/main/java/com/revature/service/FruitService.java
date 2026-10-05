package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.dtos.FruitDTO;
import com.revature.dtos.FruitRequest;
import com.revature.exceptions.FruitNotFoundException;
import com.revature.models.Fruit;
import com.revature.repositories.FruitRepository;

@Service
public class FruitService {

	private static final Logger LOG = LoggerFactory.getLogger(FruitService.class);

	private final FruitRepository fr;

	public FruitService(FruitRepository fr) {
		this.fr = fr;
	}

	@Transactional(readOnly = true)
	public List<FruitDTO> getAll() {
		List<FruitDTO> fruits = new ArrayList<>();
		for (Fruit f : fr.findAll()) {
			fruits.add(new FruitDTO(f));
		}
		return fruits;
	}

	@Transactional(readOnly = true)
	public FruitDTO getFruitById(int id) {
		return new FruitDTO(findOrThrow(id));
	}

	@Transactional(readOnly = true)
	public FruitDTO getFruitByName(String name) {
		Fruit f = fr.findFruitByName(name);
		if (f == null) {
			throw new FruitNotFoundException();
		}
		return new FruitDTO(f);
	}

	@Transactional
	public FruitDTO createFruit(FruitRequest request) {
		Fruit fruit = new Fruit();
		fruit.setName(request.getName());
		fruit.setDescription(request.getDescription());
		fruit.setPrice(request.getPrice());
		Fruit saved = fr.save(fruit);
		LOG.info("Fruit {} was created.", saved.getId());
		return new FruitDTO(saved);
	}

	@Transactional
	public FruitDTO updateFruit(int id, FruitRequest request) {
		Fruit existing = findOrThrow(id);
		existing.setName(request.getName());
		existing.setDescription(request.getDescription());
		existing.setPrice(request.getPrice());
		return new FruitDTO(existing);
	}

	@Transactional
	public void deleteFruit(int id) {
		fr.delete(findOrThrow(id));
	}

	private Fruit findOrThrow(int id) {
		return fr.findById(id).orElseThrow(FruitNotFoundException::new);
	}
}