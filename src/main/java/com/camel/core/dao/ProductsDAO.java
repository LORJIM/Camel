package com.camel.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camel.core.entities.Product;

public interface ProductsDAO extends JpaRepository<Product, Long>{
}
