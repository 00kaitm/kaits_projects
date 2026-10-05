package com.revature.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.revature.models.Fruit;
import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.repositories.FruitRepository;
import com.revature.repositories.UserRepository;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final FruitRepository fruits;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, FruitRepository fruits, PasswordEncoder encoder) {
        this.users = users;
        this.fruits = fruits;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        User henry = users.save(new User(0, "henryg", encoder.encode("123"), UserRole.ADMIN));
        User kaitlyn = users.save(new User(0, "kaitlynm", encoder.encode("321"), UserRole.ADMIN));
        User fentry = users.save(new User(0, "fentrym", encoder.encode("456"), UserRole.BASIC_USER));
        User ayan = users.save(new User(0, "ayanw", encoder.encode("654"), UserRole.BASIC_USER));

        fruits.save(new Fruit(0, "Apple", "Red delicious", 2.5, henry));
        fruits.save(new Fruit(0, "Strawberry", "Seedy goodness", 0.5, henry));
        fruits.save(new Fruit(0, "Mango", "Tropical", 2, kaitlyn));
        fruits.save(new Fruit(0, "Lime", "Sour", 1, kaitlyn));
        fruits.save(new Fruit(0, "Watermelon", "Nice Summer treat", 6, fentry));
        fruits.save(new Fruit(0, "Orange", "Citrus", 2, ayan));
    }
}