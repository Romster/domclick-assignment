package ru.romster.accounts.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import ru.romster.accounts.AccountsApplication;
import ru.romster.accounts.controller.input.RequestedSum;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by n.romanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountsApplication.class)
@WebAppConfiguration
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void ping() throws Exception {
        mockMvc.perform(get("/account/ping/"))
                .andExpect(status().is(200));
    }

    @Test
    public void fullScenario() throws Exception {
        Long id1;
        Long id2;
        //Create 2 accounts
        {
            MvcResult result1 = mockMvc.perform(put("/account/"))
                    .andExpect(status().is(200))
                    .andReturn();
            MvcResult result2 = mockMvc.perform(put("/account/"))
                    .andExpect(status().is(200))
                    .andReturn();

            id1 = Long.valueOf(result1.getResponse().getContentAsString());
            id2 = Long.valueOf(result2.getResponse().getContentAsString());
        }
        //Deposit 100 to account1. balance1 == 100
        {
            mockMvc.perform(
                    post("/account/deposit/" + id1)
                            .content(this.json(new RequestedSum(new BigDecimal(100))))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        }

        //Withdraw 49.55 from account1. balance1 == 50.45
        {
            mockMvc.perform(
                    post("/account/withdraw/" + id1)
                            .content(this.json(new RequestedSum(new BigDecimal(49.55))))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        }

        //Transfer 5.10 from account1 to account2. balance1 ==45.35, balance2 = 5.10
        {
            mockMvc.perform(
                    post("/account/transfer/" + id1 + "/" + id2)
                            .content(this.json(new RequestedSum(new BigDecimal(5.10))))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        }

        //check accounts
        {
            String accountJson1 = mockMvc.perform(get("/account/" + id1))
                    .andExpect(status().is(200))
                    .andReturn().getResponse().getContentAsString();

            String accountJson2 = mockMvc.perform(get("/account/" + id2))
                    .andExpect(status().is(200))
                    .andReturn().getResponse().getContentAsString();

            assertTrue("Account 1 json contains balance value 45.35", accountJson1.contains("45.35"));
            assertTrue("Account 2 json contains balance value  5.10", accountJson2.contains("5.10"));
        }

    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}