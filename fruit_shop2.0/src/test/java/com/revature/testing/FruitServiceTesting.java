package com.revature.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.dtos.FruitDTO;
import com.revature.dtos.FruitRequest;
import com.revature.exceptions.FruitNotFoundException;
import com.revature.models.Fruit;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.FruitRepository;
import com.revature.repositories.UserRepository;
import com.revature.service.FruitService;

public class FruitServiceTesting {

	private UserRepository userRepo;
	private FruitRepository fruitRepo;
	private FruitService fruitService;
	private Fruit f1;
	private Fruit f2;

	@BeforeEach
	public void setUp() {
		fruitRepo = mock(FruitRepository.class);
		userRepo = mock(UserRepository.class);
		fruitService = new FruitService(fruitRepo, userRepo);
		f1 = new Fruit(7, "Blueberries", "Frozen blue", 5, null);
		f2 = new Fruit(8, "Raspberries", "Wild berries", 3, null);
	}

	@Test
	public void getAllReturnsDtos() {
		when(fruitRepo.findAll()).thenReturn(Arrays.asList(f1, f2));
		assertEquals(Arrays.asList(new FruitDTO(f1), new FruitDTO(f2)), fruitService.getAll());
	}

	@Test
	public void getByIdReturnsFruit() {
		when(fruitRepo.findById(7)).thenReturn(Optional.of(f1));
		assertEquals(new FruitDTO(f1), fruitService.getFruitById(7));
	}

	@Test
	public void getByIdThrowsWhenMissing() {
		when(fruitRepo.findById(99)).thenReturn(Optional.empty());
		assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitById(99));
	}

	@Test
	public void getByNameThrowsWhenMissing() {
		when(fruitRepo.findFruitByName("Banana")).thenReturn(null);
		assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitByName("Banana"));
	}

	@Test
	public void createSetsTheOwner() {
		User owner = new User(1, "henryg", "hash", UserRole.ADMIN);
		when(userRepo.findById(1)).thenReturn(Optional.of(owner));
		when(fruitRepo.save(any(Fruit.class))).thenAnswer(inv -> inv.getArgument(0));

		FruitDTO result = fruitService.createFruit(new FruitRequest("Kiwi", "Fuzzy", 1.5), 1);

		assertEquals("henryg", result.getHidden().getUsername());
	}

	@Test
	public void getByOwnerReturnsThatUsersFruits() {
		when(fruitRepo.findByShopUserId(1)).thenReturn(Arrays.asList(f1));
		assertEquals(Arrays.asList(new FruitDTO(f1)), fruitService.getByOwner(1));
	}

	@Test
	public void updateChangesOnlyTheTargetFruit() {
		when(fruitRepo.findById(7)).thenReturn(Optional.of(f1));
		FruitDTO result = fruitService.updateFruit(7, new FruitRequest("Blueberries", "Fresh", 6));
		assertEquals(7, result.getId());
		assertEquals("Fresh", result.getDescription());
		assertEquals(6.0, result.getPrice());
	}

	@Test
	public void updateThrowsWhenMissing() {
		when(fruitRepo.findById(99)).thenReturn(Optional.empty());
		assertThrows(FruitNotFoundException.class,
				() -> fruitService.updateFruit(99, new FruitRequest("Ghost", "x", 1)));
	}

	@Test
	public void deleteRemovesTheFruit() {
		when(fruitRepo.findById(7)).thenReturn(Optional.of(f1));
		fruitService.deleteFruit(7);
		verify(fruitRepo).delete(f1);
	}
}