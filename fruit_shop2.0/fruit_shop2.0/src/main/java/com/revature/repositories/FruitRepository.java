package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Fruit;

@Repository 
public interface FruitRepository extends JpaRepository<Fruit, Integer>{
	
	public Fruit findFruitByName(String name); 

}
