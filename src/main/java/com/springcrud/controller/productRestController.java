package com.springcrud.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.springcrud.entites.product;
import com.springcrud.repos.Repository;

import jakarta.validation.Valid;



@RestController
public class productRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(productRestController.class);
	
	@Autowired
	Repository repository;
	
	@GetMapping("/products/")
	public List<product> getProduct(){
		LOGGER.info("Finding all products listed.");
		return repository.findAll();
	}
	
	@GetMapping("/products/{id}")
	@Transactional(readOnly=true)
	@Cacheable("product-cache")
	public product getProduct(@PathVariable int id) {
		LOGGER.info("Finding product by ID.");
		return repository.findById(id).get();
	}
	
	@PostMapping("/products/")
	@ResponseStatus(org.springframework.http.HttpStatus.CREATED)
	public product CreateProduct(@Valid @RequestBody product p) {
		LOGGER.info("Saving new product.");
		p.setId(null); // Ensure INSERT, not UPDATE — ignore any id sent in the request body
		return repository.save(p);
	}
	
	@PutMapping("/products/")
	public product updateProduct(@RequestBody product p) {
		LOGGER.info("Updating product by id");
		return repository.save(p);
	}
	
	@DeleteMapping("/products/{id}")
	@CacheEvict("product-cache")
	public void deleteProduct(@PathVariable int id) {
		LOGGER.info("deleted product by id");
		repository.deleteById(id);
	}
}
