package com.springcrud;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.Cache;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.springcrud.controller.productRestController;
import com.springcrud.entites.product;
import com.springcrud.repos.Repository;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;




@WebMvcTest(productRestController.class)  // ← specify controller
public class ProductRestControllerMvcTest {

	private static final String PRODUCT_URL= "/api/products/";
	private static final String CONTEXT_URL="/api";
	private static final int PRODUCT_PRICE=2500000;
	private static final String PRODUCT_DESCRIPTION_STRING="1200 cc engine";
	private static final String PRODUCT_NAME_STRING="Bike";
	private static final int  PRODUCT_ID=45;
	
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    Repository repository;

    @MockitoBean
    CacheManager cacheManager;  // ← fix the error ✅
    
    @Autowired
    ObjectMapper objectMapper;  // ← declare this


    @BeforeEach
    void setUp() {
        // ✅ configure cache so @Cacheable works in tests
        Cache mockCache = new ConcurrentMapCache("product-cache");
        when(cacheManager.getCache("product-cache")).thenReturn(mockCache);
    }

    @Test
    void testFindAll() throws Exception {

        List<product> ps = Arrays.asList(buildProduct());

        // mock service not repository
        when(repository.findAll()).thenReturn(ps);

        mockMvc.perform(get(PRODUCT_URL)
        	    .contextPath(CONTEXT_URL))
               .andExpect(status().isOk())
               //.andExpect(jsonPath("$[0].name").value("Bike"))
               .andExpect(content().json(objectMapper.writeValueAsString(ps)))
               .andDo(print());
    }
    

    
    @Test
    void testCreateProduct() throws Exception {
        product p = buildProduct();
        when(repository.save(any())).thenReturn(p);
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        mockMvc.perform(post(PRODUCT_URL)
               .contextPath(CONTEXT_URL)
               .contentType(MediaType.APPLICATION_JSON)
               .content(objWriter.writeValueAsString(p)))  // ← add this line ✅
               .andExpect(status().isCreated())
               .andExpect(content().json(objWriter.writeValueAsString(p)))
               .andDo(print());
    }
    
    @Test
    void testUpadateProduct() throws Exception {
        product p = buildProduct();
        p.setPrice(3000000);
        when(repository.save(any())).thenReturn(p);
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        mockMvc.perform(put(PRODUCT_URL)
               .contextPath(CONTEXT_URL)
               .contentType(MediaType.APPLICATION_JSON)
               .content(objWriter.writeValueAsString(p)))  // ← add this line ✅
               .andExpect(status().isOk())
               .andExpect(content().json(objWriter.writeValueAsString(p)))
               .andDo(print());
    }
    
    @Test
    void testDeleteProduct() throws Exception  {
    	doNothing().when(repository).deleteById(PRODUCT_ID);
    	mockMvc.perform(delete(PRODUCT_URL+PRODUCT_ID)
        	   .contextPath(CONTEXT_URL))
        	   .andExpect(status().isOk());
    		
    }
    
    @Test
    void testFindProductByID() throws  Exception {
    	product p = buildProduct();
    	when(repository.findById(PRODUCT_ID)).thenReturn(Optional.of(p));
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        
    	mockMvc.perform(get(PRODUCT_URL+PRODUCT_ID)
    			.contextPath(CONTEXT_URL)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objWriter.writeValueAsBytes(p)))
    			.andExpect(status().isOk())
                .andExpect(content().json(objWriter.writeValueAsString(p)))
                .andDo(print());
    }
    
    private product buildProduct() {
        product p = new product();
        p.setId(PRODUCT_ID);
        p.setName(PRODUCT_NAME_STRING);
        p.setDescription(PRODUCT_DESCRIPTION_STRING);
        p.setPrice(PRODUCT_PRICE);
        return p;
    }
}