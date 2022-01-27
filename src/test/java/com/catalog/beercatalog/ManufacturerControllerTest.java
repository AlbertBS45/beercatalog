package com.catalog.beercatalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.rest.ManufacturerController;
import com.catalog.beercatalog.service.ManufacturerService;
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
@WebMvcTest(value = ManufacturerController.class)
class ManufacturerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ManufacturerService manufacturerSv;

	Manufacturer MAN_1 = new Manufacturer(1L, "Damm", "Spain");
	Manufacturer MAN_2 = new Manufacturer(2L, "Heineken", "Netherlands");
	Manufacturer MAN_TO_INSERT = new Manufacturer(null, "Heineken", "Netherlands");
	Manufacturer MAN_NO_NAME = new Manufacturer(null, null, "Netherlands");
	Manufacturer MAN_NO_NATION = new Manufacturer(null, "Heineken", null);

	@Test
	@WithAnonymousUser
	public void getManufacturerByIdWithAnonymousUser_allowed() throws Exception {
		
		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			get("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		JSONAssert.assertEquals(objectMapper.writeValueAsString(MAN_1), result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void getManufacturerByWithAdminOrManufacturerRole_allowed() throws Exception {
		
		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			get("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		JSONAssert.assertEquals(objectMapper.writeValueAsString(MAN_1), result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithAnonymousUser
	public void addManufacturerWithAnonymousUser_unauthorized() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"MANUFACTURER"})
	public void addManufacturerWithManufacturerRole_forbidden() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void addManufacturerWithAdmin_allowed() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_TO_INSERT);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_TO_INSERT));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void addManufacturerWithoutRequiredFieldName_fail() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_NO_NAME);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_NO_NAME));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void addManufacturerWithoutRequiredFieldNationality_fail() throws Exception {

		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_NO_NATION);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_NO_NATION));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void addManufacturerNameAlreadyExists_fail() throws Exception {
		
		Mockito.when(manufacturerSv.countAllByName(Mockito.anyString())).thenReturn(1L);

		RequestBuilder requestBuilder = 
			post("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_TO_INSERT));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
	}

	@Test
	@WithAnonymousUser
	public void updateManufacturerWithAnonymousUser_unauthorized() throws Exception {

		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void updateManufacturerWithAdminRole_allowed() throws Exception {
		
		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateManufacturerWithoutRequiredFieldId_fail() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_NO_NAME);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_NO_NAME));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateManufacturerWithoutRequiredFieldName_fail() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_NO_NAME);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_NO_NAME));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateManufacturerWithoutRequiredFieldNationality_fail() throws Exception {
		
		Mockito.when(manufacturerSv.save(Mockito.any(Manufacturer.class))).thenReturn(MAN_NO_NATION);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_NO_NATION));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateManufacturerNameAlreadyExists_fail() throws Exception {
		
		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);

		Mockito.when(manufacturerSv.countAllByNameExcludingId(Mockito.anyString(), Mockito.anyLong())).thenReturn(1L);

		RequestBuilder requestBuilder = 
			put("/manufacturers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN","MANUFACTURER"})
	public void updateManufacturerNotFound() throws Exception {

		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(new Manufacturer());

		RequestBuilder requestBuilder = 
			put("/beers")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(MAN_1));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	@Test
	@WithAnonymousUser
	public void deleteManufacturerWithAnonymousUser_unauthorized() throws Exception {

		RequestBuilder requestBuilder = 
			delete("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"MANUFACTURER"})
	public void deleteManufacturerWithManufacturerRole_forbidden() throws Exception {

		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			delete("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void deleteManufacturerWithAdminRole_allowed() throws Exception {
		
		Mockito.when(manufacturerSv.findById(Mockito.anyLong())).thenReturn(MAN_1);

		RequestBuilder requestBuilder = 
			delete("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void deleteManufacturer_notFound() throws Exception {
		
		RequestBuilder requestBuilder = 
			delete("/manufacturers/1")
			.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

}
