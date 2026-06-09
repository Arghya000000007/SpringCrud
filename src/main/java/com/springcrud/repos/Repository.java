package com.springcrud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springcrud.entites.product;

public interface Repository extends JpaRepository<product, Integer> {

}
