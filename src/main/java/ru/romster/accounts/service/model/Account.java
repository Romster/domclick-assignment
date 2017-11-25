package ru.romster.accounts.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@Data
public class Account {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("balance")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal balance;


    private static class  MoneySerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
    }
}
