package com.revature.dtos;

import java.util.Objects;

import com.revature.models.Fruit;

public class FruitDTO {

	private int id;
	private String name;
	private String description;
	private double price;
	private UserDTO hidden;

	public FruitDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FruitDTO(Fruit fruit) {
		super();
		this.id = fruit.getId();
		this.name = fruit.getName();
		this.description = fruit.getDescription();
		this.price = fruit.getPrice();
		this.hidden = new UserDTO(fruit.getShopUser());
	}


	public UserDTO getHidden() {
		return hidden;
	}

	public void setHidden(UserDTO hidden) {
		this.hidden = hidden;
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
	@Override
	public int hashCode() {
		return Objects.hash(description, hidden, id, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FruitDTO other = (FruitDTO) obj;
		return Objects.equals(description, other.description) && Objects.equals(hidden, other.hidden) && id == other.id
				&& Objects.equals(name, other.name)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
	}
	@Override
	public String toString() {
		return "FruitDTO [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", hidden=" + hidden + "]";
	}

}
