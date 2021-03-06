package com.unq.dapp0.c1.comprandoencasa;

import com.unq.dapp0.c1.comprandoencasa.ComprandoEnCasaApplication;
import com.unq.dapp0.c1.comprandoencasa.webservices.AbstractRestTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ComprandoEnCasaApplicationTests extends AbstractRestTest {

    @Autowired
    private ComprandoEnCasaApplication application;

    @Autowired
    private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception{
	    assertThat(this.application).isNotNull();
	}

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/")).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Hello World!"));
    }
}
