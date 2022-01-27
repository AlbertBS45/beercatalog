package com.catalog.beercatalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.rest.BeerController;
import com.catalog.beercatalog.service.BeerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BeerController.class)
class BeerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BeerService beerSv;

	Manufacturer MAN_1 = new Manufacturer(1L, "Damm", "Spain");
	Manufacturer MAN_2 = new Manufacturer(2L, "Heineken", "Netherlands");

	Beer BEER_1 = new Beer(1L, "Equilater", "Stout", "description1", BigDecimal.valueOf(4.5), MAN_1);
	Beer BEER_2 = new Beer(2L, "Complot IPA", "IPA", "description2", BigDecimal.valueOf(6.5), MAN_1);
	Beer BEER_3 = new Beer(3L, "Daura", "Lager", "description3", BigDecimal.valueOf(5.5), MAN_1);
	
	@Test
	@WithAnonymousUser
	public void getBeerByIdWithAnonymousUser_allowed() throws Exception {
		
		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(BEER_1);

		RequestBuilder requestBuilder = 
			get("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

		JSONAssert.assertEquals(objectMapper.writeValueAsString(BEER_1), result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void getBeerByWithAdminOrManufacturerRole_allowed() throws Exception {
		
		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(BEER_1);

		RequestBuilder requestBuilder = 
			get("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder)
		.andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

		JSONAssert.assertEquals(objectMapper.writeValueAsString(BEER_1), result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void getBeerByNotFound() throws Exception {
		
		RequestBuilder requestBuilder = 
			get("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder)
		.andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	@Test
	@WithAnonymousUser
	public void addBeerWithAnonymousUser_unauthorized() throws Exception {
		
		Beer mockBeer = new Beer(null, "name", "type", "desc", BigDecimal.valueOf(5.5), null);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockBeer))
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithAdminOrManufacturerRole_allowed() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);
		Beer mockBeer = new Beer(null, "name", "type", "desc", BigDecimal.valueOf(5.5), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldName_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(null, null, "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldType_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);
		Beer mockBeer = new Beer(null, "name", null, "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldDescription_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(null, "name", "type", null, BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldAbv_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(null, "name", "type", "desc", null, mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldManufacturer_fail() throws Exception {
		
		Beer mockBeer = new Beer(null, "name", "type", "desc", BigDecimal.valueOf(4.4), null);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder =
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerWithoutRequiredFieldManufacturerId_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();

		Beer mockBeer = new Beer(null, "name", "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addBeerNameAlreadyExists_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(1L);

		Beer mockBeer = new Beer(null, "Budweiser", "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.countAllByName(Mockito.anyString())).thenReturn(1L);

		RequestBuilder requestBuilder = 
			post("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
	}

	@Test
	@WithAnonymousUser
	public void updateBeerWithAnonymousUser_unauthorized() throws Exception {
		
		Beer mockBeer = new Beer(null, "name", "type", "desc", BigDecimal.valueOf(5.5), null);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithAdminOrManufacturerRole_allowed() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);
		Beer mockBeer = new Beer(50L, "name", "type", "description", BigDecimal.valueOf(5.5), mockManufacturer);

		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(mockBeer);
		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldId_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);
		Beer mockBeer = new Beer(null, "name", "type", "description", BigDecimal.valueOf(5.5), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldName_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(50L, null, "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldType_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);
		Beer mockBeer = new Beer(50L, "name", null, "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldDescription_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(50L, "name", "type", null, BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldAbv_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(2l);

		Beer mockBeer = new Beer(50L, "name", "type", "desc", null, mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldManufacturer_fail() throws Exception {
		
		Beer mockBeer = new Beer(50L, "name", "type", "desc", BigDecimal.valueOf(4.4), null);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerWithoutRequiredFieldManufacturerId_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();

		Beer mockBeer = new Beer(50L, "name", "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.save(Mockito.any(Beer.class))).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerNameAlreadyExists_fail() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(1L);

		Beer mockBeer = new Beer(1L, "Budweiser", "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(mockBeer);

		Mockito.when(beerSv.countAllByNameExcludingId(Mockito.anyString(), Mockito.anyLong())).thenReturn(1L);

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateBeerNotFound() throws Exception {
		
		Manufacturer mockManufacturer = new Manufacturer();
		mockManufacturer.setId(1L);

		Beer mockBeer = new Beer(1L, "Budweiser", "type", "desc", BigDecimal.valueOf(4.4), mockManufacturer);

		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(Mockito.any(Beer.class));

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(mockBeer));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	@Test
	@WithAnonymousUser
	public void deleteBeerWithAnonymousUser_unauthorized() throws Exception {

		RequestBuilder requestBuilder = 
			delete("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void deleteBeerWithAdminOrManufacturerRole_allowed() throws Exception {
		
		Beer mockBeer = new Beer(50L, "name", "type", "desc", BigDecimal.valueOf(4.4), null);

		Mockito.when(beerSv.findById(Mockito.anyLong())).thenReturn(mockBeer);

		RequestBuilder requestBuilder = 
			delete("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void deleteBeer_notFound() throws Exception {
		
		RequestBuilder requestBuilder = 
			delete("/beers/50")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

}
