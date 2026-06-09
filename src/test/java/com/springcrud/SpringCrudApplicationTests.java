package com.springcrud;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestTemplate;

import com.springcrud.entites.product;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class SpringCrudApplicationTests {
	@Value("${productapi.service.url}")
	private String baseUrl;
	
	@Test
	void testGetProduct() {
		RestTemplate restTemplate = new RestTemplate();
		product response =restTemplate.getForObject(baseUrl+"5",product.class);
		System.out.println(response.getName());
		assertNotNull(response);
		
	}
	
	@Test
	void testCreateProduct() {
		RestTemplate restTemplate = new RestTemplate();
		product p = new product();
		p.setName("Camera");
		p.setDescription("50 megapixel");
		p.setPrice(50000);
		product newProduct=restTemplate.postForObject(baseUrl, p, product.class);
		assertNotNull(newProduct);
	}
	
	@Test
	void testUpdateProduct() {
		RestTemplate restTemplate = new RestTemplate();
		product response =restTemplate.getForObject(baseUrl+"7",product.class);
		assertNotNull(response);
		response.setPrice(55000);
		restTemplate.put(baseUrl,response);
	}
	// Add this test
	@Test
	void testDeleteProduct() {
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.delete(baseUrl + "3");
	    System.out.println("Deleted successfully!");
	}
	
	@Test
	void testCaching() {
	    RestTemplate restTemplate = new RestTemplate();

	    // First call → hits DB
	    product p1 = restTemplate.getForObject(baseUrl + "5", product.class);
	    System.out.println("First call: " + p1.getName());

	    // Second call SAME ID → should use cache
	    product p2 = restTemplate.getForObject(baseUrl + "5", product.class);
	    System.out.println("Second call: " + p2.getName());

	    // If cache works → only ONE Hibernate SQL for id=5
	    // If cache fails → TWO Hibernate SQL for id=5
	}

}
