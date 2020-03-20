package recruitment.roche;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductEndpointTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @DirtiesContext
    public void shouldCreateAndFindProduct() throws Exception {
        //when
        ProductRequestDTO requestDTO = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(202.01));
        hitPostWith(requestDTO)
                .andExpect(status().is2xxSuccessful());

        String getResult = hitGetWith(requestDTO.getSku())
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        //then
        ProductResponseDTO productFromGet = fromJson(getResult, ProductResponseDTO.class);

        Assertions.assertEquals(requestDTO.getSku(), productFromGet.getSku());
        Assertions.assertEquals(requestDTO.getName(), productFromGet.getName());
        Assertions.assertEquals(requestDTO.getPrice(), productFromGet.getPrice());
        Assertions.assertTrue(productFromGet.getCreatedAt() <= System.currentTimeMillis());
    }


    @Test
    @DirtiesContext
    public void shouldReturnBadRequestWhenCreatingProductWithTheSameSKU() throws Exception {
        //when
        ProductRequestDTO dto = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(202.01));
        hitPostWith(dto).andExpect(status().is2xxSuccessful());
        hitPostWith(dto).andExpect(status().isBadRequest());
    }


    @Test
    @DirtiesContext
    public void shouldCreateAndUpdateProduct() throws Exception {
        //when
        ProductRequestDTO dtoToCreate = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(202.01));
        hitPostWith(dtoToCreate)
                .andExpect(status().is2xxSuccessful());

        ProductRequestDTO dtoToUpdate = new ProductRequestDTO("some-sku", "some-name2", BigDecimal.valueOf(201.01));
        hitPutWith(dtoToUpdate)
                .andExpect(status().is2xxSuccessful());


        ProductResponseDTO resultFromGet = fromJson(hitGetWith(dtoToCreate.getSku()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString(), ProductResponseDTO.class);

        //then
        Assertions.assertEquals(dtoToUpdate.getSku(), resultFromGet.getSku());
        Assertions.assertEquals(dtoToUpdate.getName(), resultFromGet.getName());
        Assertions.assertEquals(dtoToUpdate.getPrice(), resultFromGet.getPrice());
    }

    @Test
    @DirtiesContext
    public void shouldReturnNotFoundWhenGettingNonExistingSku() throws Exception {
        //when
        hitGetWith("non-existing-sku").andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    public void shouldReturnListOfProducts() throws Exception {
        //when
        hitPostWith(new ProductRequestDTO("some-sku1", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());
        hitPostWith(new ProductRequestDTO("some-sku2", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());
        hitPostWith(new ProductRequestDTO("some-sku3", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());

        List<ProductResponseDTO> list = fromJson(hitGet().andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString(), List.class);

        Assertions.assertTrue(list.size() == 3);
    }

    @Test
    @DirtiesContext
    public void shouldReturnOnlyActiveProducts() throws Exception {
        //when
        hitPostWith(new ProductRequestDTO("some-sku1", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());
        hitPostWith(new ProductRequestDTO("some-sku2", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());
        hitPostWith(new ProductRequestDTO("some-sku3", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());

        hitDeleteWith("some-sku2");

        List<ProductResponseDTO> list = fromJson(hitGet().andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString(), new TypeReference<List<ProductResponseDTO>>() {});

        Assertions.assertEquals(2, list.size());
        Assertions.assertFalse(list.stream().anyMatch(it -> it.getSku().equals("some-sku2")));
    }


    @Test
    @DirtiesContext
    public void shouldNotFindDeletedProduct() throws Exception {
        //given
        hitPostWith(new ProductRequestDTO("some-sku1", "some-name", BigDecimal.valueOf(202.01))).andExpect(status().is2xxSuccessful());
        hitDeleteWith("some-sku1");

        //when
        hitGetWith("some-sku1").andExpect(status().isNotFound());
    }

    private ResultActions hitDeleteWith(String sku) throws Exception {
        return mockMvc
                .perform(delete("/products/" + sku).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

    }

    private ResultActions hitGet() throws Exception {
        return mockMvc
                .perform(get("/products/").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions hitPostWith(ProductRequestDTO dto) throws Exception {
        return mockMvc
                .perform(post("/products/").content(toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions hitPutWith(ProductRequestDTO dto) throws Exception {
        return mockMvc
                .perform(put("/products/").content(toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions hitGetWith(String sku) throws Exception {
        return mockMvc
                .perform(get("/products/" + sku).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


    private String toJson(ProductRequestDTO dto) {
        try {
            return new ObjectMapper().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(String dto, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(dto, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(String dto, TypeReference<T> valueType) {
        try {
            return new ObjectMapper().readValue(dto, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}