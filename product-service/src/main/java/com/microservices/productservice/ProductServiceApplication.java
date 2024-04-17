package com.microservices.productservice;

import com.microservices.productservice.model.Product;
import com.microservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class ProductServiceApplication implements CommandLineRunner {

	private final ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (productRepository.count() < 1) {
			Product product = new Product();
			product.setName("iPhone 15");
			product.setDescription("iPhone 15");
			product.setPrice(BigDecimal.valueOf(1000));

			productRepository.save(product);
		}
	}
}
