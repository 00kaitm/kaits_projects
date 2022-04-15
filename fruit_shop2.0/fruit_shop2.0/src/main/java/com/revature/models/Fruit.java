package com.revature.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Check;


@Entity
@Table(name="fruit")
@Check(constraints = "price > 0")

public class Fruit {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id; 
	@Column(nullable = false, unique = true)
	private String name; 
	@Column
	private String description; 
	@Column
	private double price; 
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User shopUser;

	public Fruit() {
		super();
	}

	public Fruit(int id, String name, String description, double price, User shopUser) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.shopUser = shopUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public User getShopUser() {
		return shopUser;
	}

	public void setShopUser(User shopUser) {
		this.shopUser = shopUser;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, price, shopUser);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fruit other = (Fruit) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(shopUser, other.shopUser);
	}

	@Override
	public String toString() {
		return "Fruit [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", shopUser=" + shopUser + "]";
	}

	
	
	
	
	
}
