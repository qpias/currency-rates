package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.MockitoAnnotations.initMocks;
import org.junit.jupiter.api.TestInstance;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "key=bar")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class RateControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  Cache mockCache;
 
  @InjectMocks
  RateController controller;

  @Test
  public void noParamsCalculationShouldReturnOne() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());
    this.mockMvc.perform(get("/rates")).andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("$.result").value("1.0"));
  }

  @Test
  public void fromParamShouldReturnCorrectSum() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());		
    this.mockMvc.perform(get("/rates").param("from", "USD"))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("$.result").value("0.9323504721265975"));
  }
  
  @Test
  public void toParamShouldReturnCorrectSum() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());		
    this.mockMvc.perform(get("/rates").param("to", "USD"))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("$.result").value("1.072558045387268"));
  }

  @Test
  public void amountParamShouldReturnCorrectSum() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());		
    this.mockMvc.perform(get("/rates").param("amount", "50"))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("$.result").value("50.0"));
}

  @Test
  public void incorrectCurrencyShouldProduceError() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());		
    this.mockMvc.perform(get("/rates").param("from", "USDP"))
      .andDo(print()).andExpect(status().isNotFound())
      .andExpect(content().string("Incorrect fromRate"));
  }

  @Test
  public void incorrectAmountShouldProduceError() throws Exception {
    when(mockCache.getRates()).thenReturn(getRates());		
    this.mockMvc.perform(get("/rates").param("amount", "USDP"))
      .andDo(print()).andExpect(status().isNotFound())
      .andExpect(content().string("Failed to convert value of type 'java.lang.String' to required type 'double'; For input string: \"USDP\""));
  }

  private Rates getRates() throws Exception {
    Resource stateFile = new ClassPathResource("rates.json");
    ObjectMapper mapper = new ObjectMapper();
    Rates rates = mapper.readValue(stateFile.getFile(), Rates.class);
    return rates;
  }
}
