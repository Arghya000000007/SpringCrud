package com.springcrud.entites;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer  id;
	
	@NotNull
	private String name;
	
	@Size(max=100)
	private String description;
	
	@Min(1)
	private Integer price;
	
	public product(){
		
	}
	public product(Integer id, String name, String description, Integer price){
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}
	/**
	 * @param price2 the price to set
	 */
	public void setPrice(Integer price2) {
		this.price = price2;
	}
}
