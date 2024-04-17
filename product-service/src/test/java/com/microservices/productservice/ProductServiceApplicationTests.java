package com.microservices.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry){
		dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		String productRequestString = objectMapper.writeValueAsString(getProductRequest());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	@Test
	public void shouldGetAllProducts() throws Exception {
		String productRequestString = objectMapper.writeValueAsString(getProductRequest());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		String responseString = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<ProductResponse> productResponseList = objectMapper.readValue(responseString, new TypeReference<>() {});

		Assertions.assertFalse(productResponseList.isEmpty(), "Product list should not be empty");
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 15")
				.description("iPhone 15")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
