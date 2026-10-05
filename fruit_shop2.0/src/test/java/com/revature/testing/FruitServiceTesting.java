package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.exceptions.FruitNotFoundException;
import com.revature.models.Fruit;
import com.revature.repositories.FruitRepository;
import com.revature.service.FruitService;


@ExtendWith(MockitoExtension.class)
public class FruitServiceTesting {

	static FruitRepository fruitRepo; 
	static FruitService fruitService; 
	static List<Fruit> fruits = new ArrayList<>(); 
	static Fruit f1; 
	static Fruit f2;
	static Fruit f3; 
	
	
	@BeforeAll
	public static void set() {
		fruitRepo = mock(FruitRepository.class);
		fruitService = new FruitService(fruitRepo); 
		f1 = new Fruit(7, "Blueberries", "Frozen blue", 5, null); 
		f2 = new Fruit(8, "Raspberries", "Wild berries", 3, null); 
		f3 = new Fruit(9, "Pineapple", "Sweet tropical", 7, null); 
		fruits.add(f1);
		fruits.add(f2);
		fruits.add(f3);
	}
	
	@Test
	public void getAllFruitsTest() {
		when(fruitRepo.findAll()).thenReturn(fruits); 
		assertEquals(fruits, fruitService.getAll());
	}
	
	@Test 
	public void getFruitByIdTest() throws FruitNotFoundException {
		when(fruitRepo.findById(1)).thenReturn(Optional.of(f1)); 
		assertEquals(f1, fruitService.getFruitById(1));
	} 
	
	@Test
	public void getFruitByNameTest() throws FruitNotFoundException {
		when(fruitRepo.findFruitByName("Raspberries")).thenReturn(f2); 
		assertEquals(f2, fruitService.getFruitByName("Raspberries")); 
	}
	 
	@Test 
	public void createFruitTest() {
		when(fruitRepo.save(f1)).thenReturn(f1); 
		assertEquals(f1, fruitService.createFruit(f1)); 
	}
	
	@Test
	public void updateFruitTest() throws FruitNotFoundException {
		when(fruitRepo.findById(1)).thenReturn(Optional.of(f1)); 
		when(fruitRepo.save(f1)).thenReturn(f1); 
		assertEquals(f1, fruitService.updateFruit(1, f1));
	}
	
	@Test 
	public void deleteFruitTest() throws FruitNotFoundException {
		when(fruitRepo.getById(1)).thenReturn(f1); 
		assertEquals(true, fruitService.deleteFruit(1)); 
	}
	
	
}
